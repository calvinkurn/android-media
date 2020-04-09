package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
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
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.di.PreferenceListComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class PreferenceListFragment : BaseDaggerFragment(), PreferenceListAdapter.PreferenceListAdapterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferencelistAnalytics: PreferenceListAnalytics

    private val viewModel: PreferenceListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[PreferenceListViewModel::class.java]
    }

    private val adapter = PreferenceListAdapter(this)

    private var progressDialog: AlertDialog? = null
    private val swipeRefreshLayout by lazy { view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout) }
    private val mainContent by lazy { view?.findViewById<ConstraintLayout>(R.id.main_content) }
    private val preferenceList by lazy { view?.findViewById<RecyclerView>(R.id.rv_preference_list) }
    private val progressBar by lazy { view?.findViewById<ProgressBar>(R.id.progress_bar) }
    private val buttonPreferenceListAction by lazy { view?.findViewById<UnifyButton>(R.id.btn_preference_list_action) }

    private val ivEmptyState by lazy { view?.findViewById<ImageView>(R.id.iv_empty) }
    private val tvHeaderEmptyState by lazy { view?.findViewById<Typography>(R.id.tv_header_empty) }
    private val tvSubtitleEmptyState by lazy { view?.findViewById<Typography>(R.id.tv_subtitle_empty) }
    private val emptyStateGroup by lazy { view?.findViewById<Group>(R.id.group_empty_state) }

    private val globalError by lazy { view?.findViewById<GlobalError>(R.id.global_error) }

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
                    swipeRefreshLayout?.isRefreshing = false
                    globalError?.gone()
                    mainContent?.visible()
                    val profiles = it.data.profiles ?: ArrayList()
                    val maxProfiles = it.data.maxProfile
                    adapter.submitList(profiles)
                    if (profiles.isEmpty()) {
                        ImageHandler.LoadImage(ivEmptyState, EMPTY_STATE_PREFERENCE_PICT)
                        tvHeaderEmptyState?.setText(R.string.preference_list_empty_header)
                        tvSubtitleEmptyState?.setText(R.string.preference_list_empty_subtitle)
                        emptyStateGroup?.visible()
                        preferenceList?.gone()
                        buttonPreferenceListAction?.isEnabled = true
                        buttonPreferenceListAction?.setText(R.string.add_first_preference)
                    } else if (profiles.isNotEmpty() && profiles.size >= maxProfiles) {
                        emptyStateGroup?.gone()
                        preferenceList?.visible()
                        buttonPreferenceListAction?.setText(R.string.add_preference)
                        buttonPreferenceListAction?.isEnabled = false
                    } else {
                        emptyStateGroup?.gone()
                        preferenceList?.visible()
                        buttonPreferenceListAction?.isEnabled = true
                        buttonPreferenceListAction?.setText(R.string.add_preference)
                    }
                }
                is OccState.Fail -> {
                    if (!it.isConsumed) {
                        swipeRefreshLayout?.isRefreshing = false
                        if (it.throwable != null) {
                            handleError(it.throwable)
                        }
                    }
                }
                else -> swipeRefreshLayout?.isRefreshing = true
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
                }
                else -> {
                    context?.let { ctx ->
                        if (progressDialog == null) {
                            progressDialog = AlertDialog.Builder(ctx)
                                    .setView(R.layout.purchase_platform_progress_dialog_view)
                                    .setCancelable(false)
                                    .create()
                        }
                        progressDialog?.show()
                    }
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
        globalError?.setType(type)
        globalError?.setActionClickListener {
            viewModel.getAllPreference()
        }
        mainContent?.gone()
        globalError?.visible()
    }

    private fun initViews() {
        buttonPreferenceListAction?.setOnClickListener {
            preferencelistAnalytics.eventAddPreferenceFromPurchaseSetting()
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT)
            intent.putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, getString(R.string.preference_number_summary) + " " + (adapter.itemCount + 1))
            startActivityForResult(intent, REQUEST_CREATE_PREFERENCE)
        }
        preferenceList?.adapter = adapter
        preferenceList?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        preferenceList?.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.left = context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
                outRect.right = context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
                outRect.top = context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
                outRect.bottom = context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
            }
        })
    }

    override fun onPreferenceSelected(preference: ProfilesItemModel) {
        preferencelistAnalytics.eventClickJadikanPilihanUtama()
        viewModel.changeDefaultPreference(preference)
    }

    override fun onPreferenceEditClicked(preference: ProfilesItemModel, position: Int, profileSize: Int) {
        preferencelistAnalytics.eventClickSettingPreferenceGearInPreferenceListPage()
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT).apply {
            putExtra(PreferenceEditActivity.EXTRA_SHOW_DELETE_BUTTON, true)
            putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, getString(R.string.lbl_summary_preference_option) + " " + position)
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

        private const val EMPTY_STATE_PREFERENCE_PICT = "https://ecs7.tokopedia.net/android/others/beli_langsung_intro.png"
    }
}
