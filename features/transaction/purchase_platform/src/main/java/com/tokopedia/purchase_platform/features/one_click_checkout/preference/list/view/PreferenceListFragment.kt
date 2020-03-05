package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus.GATEWAY_TIMEOUT
import com.tokopedia.globalerror.ReponseStatus.INTERNAL_SERVER_ERROR
import com.tokopedia.globalerror.ReponseStatus.NOT_FOUND
import com.tokopedia.globalerror.ReponseStatus.REQUEST_TIMEOUT
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ProfilesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.di.PreferenceListComponent
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_preference_list.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class PreferenceListFragment : BaseDaggerFragment(), PreferenceListAdapter.PreferenceListAdapterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PreferenceListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[PreferenceListViewModel::class.java]
    }

    private val adapter = PreferenceListAdapter(this)

    private var progressDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preference_list, container, false)
    }

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(PreferenceListComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_PREFERENCE || requestCode == REQUEST_CREATE_PREFERENCE) {
            val message = data?.getStringExtra(PreferenceEditActivity.EXTRA_RESULT_MESSAGE)
            if (message != null && message.isNotBlank()) {
                view?.let {
                    Toaster.make(it, message)
                }
            }
            viewModel.getAllPreference()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initViews()
        viewModel.getAllPreference()
    }

    private fun initViewModel() {
        viewModel.preferenceList.observe(this, Observer {
            when (it) {
                is OccState.Success -> {
                    swipe_refresh_layout.isRefreshing = false
                    global_error.gone()
                    main_content.visible()
                    val profiles = it.data.profiles ?: ArrayList()
                    adapter.submitList(profiles)
                    if (profiles.isEmpty()) {
                        group_empty_state.visible()
                        rv_preference_list.gone()
                        btn_preference_list_action.setText(R.string.add_first_preference)
                    } else {
                        group_empty_state.gone()
                        rv_preference_list.visible()
                        btn_preference_list_action.setText(R.string.add_preference)
                    }
                }
                is OccState.Fail -> {
                    if (!it.isConsumed) {
                        swipe_refresh_layout.isRefreshing = false
                        if (it.throwable != null) {
                            handleError(it.throwable)
                        }
                    }
                }
                else -> swipe_refresh_layout.isRefreshing = true
            }
        })
        viewModel.setDefaultPreference.observe(this, Observer {
            when (it) {
                is OccState.Success -> {
                    progressDialog?.dismiss()
                    view?.let { view ->
                        if (it.data.messages.isNotEmpty()) {
                            Toaster.make(view, it.data.messages[0])
                        } else {
                            Toaster.make(view, "Success")
                        }
                    }
                }
                is OccState.Fail -> {
//                    if (!it.isConsumed) {
                    progressDialog?.dismiss()
                    view?.let { view ->
                        if (it.throwable != null) {
                            if (it.throwable is MessageErrorException) {
                                Toaster.make(view, it.throwable.message
                                        ?: "Failed", type = Toaster.TYPE_ERROR)
                            } else {
                                Toaster.make(view, it.throwable.localizedMessage
                                        ?: "Failed", type = Toaster.TYPE_ERROR)
                            }
                        } else {
                            Toaster.make(view, "Failed", type = Toaster.TYPE_ERROR)
                        }
                    }
//                        viewModel.consumeSetDefaultPreferenceFail()
//                    }
                }
                else -> {
                    if (progressDialog == null) {
                        progressDialog = AlertDialog.Builder(context!!)
                                .setView(R.layout.purchase_platform_progress_dialog_view)
                                .setCancelable(false)
                                .create()
                    }
                    progressDialog?.show()
                }
            }
        })
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage.toIntOrNull()) {
                    GATEWAY_TIMEOUT, REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.make(it, "Terjadi kesalahan pada server. Ulangi beberapa saat lagi", type = Toaster.TYPE_ERROR)
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.make(it, throwable.message
                            ?: "Terjadi kesalahan pada server. Ulangi beberapa saat lagi", type = Toaster.TYPE_ERROR)
                }
            }
        }
        viewModel.consumePreferenceListFail()
    }

    private fun showGlobalError(type: Int) {
        global_error.setType(type)
        global_error.setActionClickListener {
            viewModel.getAllPreference()
        }
        main_content.gone()
        global_error.visible()
    }

    private fun initViews() {
        btn_preference_list_action.setOnClickListener {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT)
            intent.putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, adapter.itemCount + 1)
            startActivityForResult(intent, REQUEST_CREATE_PREFERENCE)
        }
        rv_preference_list.adapter = adapter
        rv_preference_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_preference_list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
//                val position = parent.getChildAdapterPosition(view)
//                val lastIndex = (parent.adapter?.itemCount ?: 1) - 1
//                if (position >= lastIndex) {
//                    outRect.bottom =
//                }
                outRect.left = context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
                outRect.right = context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
                outRect.top = context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
                outRect.bottom = context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
            }
        })
    }

    override fun onPreferenceSelected(preference: ProfilesItemModel) {
        viewModel.changeDefaultPreference(preference)
    }

    override fun onPreferenceEditClicked(preference: ProfilesItemModel, position: Int) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT)
        intent.apply {
            putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, position)
            putExtra(PreferenceEditActivity.EXTRA_PROFILE_ID, preference.profileId)
            putExtra(PreferenceEditActivity.EXTRA_ADDRESS_ID, preference.addressModel?.addressId)
            putExtra(PreferenceEditActivity.EXTRA_SHIPPING_ID, preference.shipmentModel?.serviceId)
            putExtra(PreferenceEditActivity.EXTRA_GATEWAY_CODE, preference.paymentModel?.gatewayCode)
        }
        startActivityForResult(intent, REQUEST_EDIT_PREFERENCE)
    }

    companion object {

        const val REQUEST_EDIT_PREFERENCE = 11
        const val REQUEST_CREATE_PREFERENCE = 12
    }
}
