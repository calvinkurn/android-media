package com.tokopedia.wishlistcollection.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Keep
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.databinding.FragmentWishlistCollectionEditBinding
import com.tokopedia.wishlistcollection.analytics.WishlistCollectionAnalytics
import com.tokopedia.wishlistcollection.data.params.UpdateWishlistCollectionParams
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionByIdResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionNamesResponse
import com.tokopedia.wishlistcollection.di.DaggerWishlistCollectionComponent
import com.tokopedia.wishlistcollection.di.WishlistCollectionModule
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.COLLECTION_ID
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.COLLECTION_NAME
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionEditAdapter
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetUpdateWishlistCollectionName
import com.tokopedia.wishlistcollection.view.viewmodel.WishlistCollectionEditViewModel
import javax.inject.Inject

@Keep
class WishlistCollectionEditFragment :
    BaseDaggerFragment(),
    WishlistCollectionEditAdapter.ActionListener {
    override fun getScreenName(): String = ""
    private var binding by autoClearedNullable<FragmentWishlistCollectionEditBinding>()
    private val userSession: UserSessionInterface by lazy { UserSession(activity) }
    private var _existingCollectionName = ""
    private var _collectionId = ""
    private var newCollectionName = ""
    private var newAccessId = 0
    private var _existingAccessId = 0
    private var hasCollectionNameChanges = false
    private var hasAccessChanges = false
    private var listCollections: List<GetWishlistCollectionNamesResponse.GetWishlistCollectionNames.DataItem> = emptyList()
    private val wishlistCollectionEditAdapter = WishlistCollectionEditAdapter()
    private val handler = Handler(Looper.getMainLooper())
    private val checkNameRunnable = Runnable {
        checkIsCollectionNameExists(newCollectionName)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val wishlistCollectionEditViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[WishlistCollectionEditViewModel::class.java]
    }

    override fun initInjector() {
        activity?.let { activity ->
            DaggerWishlistCollectionComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .wishlistCollectionModule(WishlistCollectionModule(activity))
                .build()
                .inject(this)
        }
    }

    private fun getBaseAppComponent(): BaseAppComponent {
        return (activity?.application as BaseMainApplication).baseAppComponent
    }

    companion object {
        private const val DELAY_CHECK_NAME = 500L

        @JvmStatic
        fun newInstance(bundle: Bundle): WishlistCollectionEditFragment {
            return WishlistCollectionEditFragment().apply {
                arguments = bundle.apply {
                    putString(COLLECTION_ID, this.getString(COLLECTION_ID))
                    putString(COLLECTION_NAME, this.getString(COLLECTION_NAME))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _existingCollectionName = arguments?.getString(COLLECTION_NAME) ?: ""
        _collectionId = arguments?.getString(COLLECTION_ID) ?: ""
        checkLogin()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishlistCollectionEditBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        observingData()
    }

    private fun prepareLayout() {
        binding?.run {
            tfCollectionName.editText.setText(_existingCollectionName)
            tfCollectionName.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    newCollectionName = p0.toString().trimStart().trimEnd()
                    if (newCollectionName.isNotEmpty()) {
                        handler.postDelayed(checkNameRunnable, DELAY_CHECK_NAME)
                    } else {
                        hasCollectionNameChanges = false
                        updateSaveButton()
                    }
                }
            })
            collectionSaveButton.apply {
                isEnabled = false
                text = getString(R.string.collection_save_to_existing_collection)
            }
            collectionDeleteButton.setOnClickListener {
                showDialogDeleteCollection(_collectionId, newCollectionName.ifEmpty { _existingCollectionName })
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (hasCollectionNameChanges || hasAccessChanges) {
                        showDialogExitConfirmation()
                    } else {
                        activity?.finish()
                    }
                }
            }
        )
    }

    private fun showDialogExitConfirmation() {
        val dialog = context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.collection_edit_back_dialog_confirmation_title))
                setDescription(getString(R.string.collection_edit_back_dialog_confirmation_desc))
                setPrimaryCTAText(getString(R.string.collection_edit_back_dialog_confirmation_primary_button))
                setPrimaryCTAClickListener {
                    dismiss()
                    activity?.finish()
                }
                setSecondaryCTAText(getString(R.string.collection_edit_back_dialog_confirmation_secondary_button))
                setSecondaryCTAClickListener {
                    dismiss()
                }
            }
        }
        dialog?.show()
    }

    private fun checkLogin() {
        if (userSession.isLoggedIn) {
            getWishlistCollectionById()
        } else {
            startActivityForResult(
                RouteManager.getIntent(context, ApplinkConst.LOGIN),
                BottomSheetUpdateWishlistCollectionName.REQUEST_CODE_LOGIN
            )
        }
    }

    private fun showDialogDeleteCollection(collectionId: String, collectionName: String) {
        val dialog =
            context?.let { DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(
            getString(
                R.string.wishlist_collection_detail_delete_confirmation_title,
                collectionName
            )
        )
        dialog?.setDescription(getString(R.string.wishlist_collection_detail_delete_confirmation_desc))
        dialog?.setPrimaryCTAText(getString(R.string.wishlist_delete_label))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            doDeleteCollection(collectionId)
        }
        dialog?.setSecondaryCTAText(getString(R.string.wishlist_cancel_manage_label))
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        dialog?.show()
    }

    private fun doDeleteCollection(collectionId: String) {
        wishlistCollectionEditViewModel.deleteWishlistCollection(collectionId)
        WishlistCollectionAnalytics.sendClickHapusOnCollectionFolderEvent()
    }

    private fun getWishlistCollectionById() {
        wishlistCollectionEditViewModel.getWishlistCollectionById(_collectionId)
    }

    private fun getWishlistCollectionNames() {
        wishlistCollectionEditViewModel.getWishlistCollectionNames()
    }

    private fun observingData() {
        observingGetCollectionById()
        observeCollectionNames()
        observeUpdateCollection()
        observingDeleteWishlistCollection()
    }

    private fun checkIsCollectionNameExists(checkName: String) {
        if (checkName.isEmpty()) {
            hasCollectionNameChanges = false
            updateSaveButton()
        } else {
            if (listCollections.isNotEmpty()) {
                run check@{
                    listCollections.forEach { item ->
                        if (checkName.lowercase() == item.name.lowercase()) {
                            binding?.run {
                                if (checkName != _existingCollectionName) {
                                    tfCollectionName.isInputError = true
                                    val labelMessage = context?.getString(R.string.collection_create_bottomsheet_name_error) ?: ""
                                    tfCollectionName.setMessage(labelMessage)
                                }
                                hasCollectionNameChanges = false
                                updateSaveButton()
                                return@check
                            }
                        } else {
                            binding?.run {
                                tfCollectionName.isInputError = false
                                tfCollectionName.setMessage("")
                                hasCollectionNameChanges = true
                                updateSaveButton()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateSaveButton() {
        binding?.run {
            collectionSaveButton.apply {
                text = getString(R.string.collection_save_to_existing_collection)
                if (hasCollectionNameChanges || hasAccessChanges) {
                    isEnabled = true
                    setOnClickListener { updateCollection() }
                } else {
                    isEnabled = false
                    setOnClickListener { }
                }
            }
        }
    }

    private fun observingGetCollectionById() {
        wishlistCollectionEditViewModel.getWishlistCollectionByIdResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == WishlistCollectionConsts.OK) {
                        _existingAccessId = result.data.data.collection.access
                        getWishlistCollectionNames()
                        updateLayout(result.data.data)
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            context?.getString(
                                R.string.wishlist_common_error_msg
                            )
                        }
                        errorMessage?.let { showToaster(it, "", Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observeCollectionNames() {
        wishlistCollectionEditViewModel.collectionNames.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == WishlistCollectionConsts.OK) {
                        listCollections = result.data.data
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            context?.getString(
                                R.string.wishlist_common_error_msg
                            )
                        }
                        errorMessage?.let { showToaster(it, "", Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observeUpdateCollection() {
        wishlistCollectionEditViewModel.updateWishlistCollectionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == WishlistCollectionConsts.OK) {
                        val intent = Intent()
                        intent.putExtra(
                            ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS,
                            true
                        )
                        intent.putExtra(
                            ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER,
                            result.data.data.message
                        )
                        activity?.setResult(Activity.RESULT_OK, intent)
                        activity?.finish()
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            context?.getString(
                                R.string.wishlist_common_error_msg
                            )
                        }
                        errorMessage?.let { showToaster(it, "", Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observingDeleteWishlistCollection() {
        wishlistCollectionEditViewModel.deleteCollectionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == WishlistCollectionFragment.OK && result.data.data.success) {
                        val intent = Intent()
                        intent.putExtra(
                            ApplinkConstInternalPurchasePlatform.NEED_FINISH_ACTIVITY,
                            true
                        )
                        intent.putExtra(
                            ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS,
                            true
                        )
                        intent.putExtra(
                            ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER,
                            result.data.data.message
                        )
                        activity?.setResult(Activity.RESULT_OK, intent)
                        activity?.finish()
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            context?.getString(
                                R.string.wishlist_common_error_msg
                            )
                        }
                        errorMessage?.let { showToaster(it, "", Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun updateLayout(data: GetWishlistCollectionByIdResponse.GetWishlistCollectionById.Data) {
        var tickerDesc = "<html>\n" +
            "        <body> ${data.ticker.title} \n" +
            "<ul>"

        data.ticker.descriptions.forEach {
            tickerDesc += "<li> $it </li>"
        }

        tickerDesc += "</ul></body>\n" +
            "        </html>"

        binding?.run {
            tfCollectionName.editText.setText(data.collection.name)
            tickerPublic.setHtmlDescription(tickerDesc)

            if (data.collection.access == 2) {
                tickerPublic.visible()
            } else {
                tickerPublic.gone()
            }
            rvAccessOptions.adapter = wishlistCollectionEditAdapter
            rvAccessOptions.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            wishlistCollectionEditAdapter.apply {
                setActionListener(this@WishlistCollectionEditFragment)
                addList(data.accessOptions)
                setCurrentAccess(data.collection.access)
            }
        }
    }

    private fun updateCollection() {
        val params = UpdateWishlistCollectionParams(
            id = _collectionId.toLongOrZero(),
            name = newCollectionName.ifEmpty { _existingCollectionName },
            access = if (newAccessId == 0) _existingAccessId.toLong() else newAccessId.toLong()
        )
        wishlistCollectionEditViewModel.updateAccessWishlistCollection(params)
    }

    private fun showToaster(message: String, actionText: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_LONG, type, actionText).show()
        }
    }

    override fun onOptionAccessItemClicked(accessId: Int) {
        newAccessId = accessId
        if (accessId == 2) {
            showTickerAccessInfo()
        } else {
            hideTickerAccessInfo()
        }
        hasAccessChanges = newAccessId != _existingAccessId
        updateSaveButton()
    }

    private fun showTickerAccessInfo() {
        binding?.run { tickerPublic.visible() }
    }

    private fun hideTickerAccessInfo() {
        binding?.run { tickerPublic.gone() }
    }
}
