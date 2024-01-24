package com.tokopedia.shareexperience.ui.util

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.data.analytic.ShareExAnalytics
import com.tokopedia.shareexperience.data.di.DaggerShareExComponent
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.request.affiliate.ShareExAffiliateEligibilityRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetAffiliateEligibilityUseCase
import com.tokopedia.shareexperience.ui.ShareExBottomSheet
import com.tokopedia.shareexperience.ui.listener.ShareExBottomSheetListener
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExInitializerArg
import com.tokopedia.shareexperience.ui.uistate.ShareExInitializationUiState
import com.tokopedia.shareexperience.ui.view.ShareExLoadingDialog
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * This class is used to prepare the share feature
 * Put anything that share feature needed to do before showing the UI in here
 */
class ShareExInitializer(
    activity: FragmentActivity
) : DefaultLifecycleObserver, ShareExBottomSheetListener {

    private val weakActivity = WeakReference(activity)
    private var dialog: ShareExLoadingDialog? = null
    private var bottomSheet: ShareExBottomSheet? = null
    private var bottomSheetArg: ShareExBottomSheetArg? = null

    init {
        addObserver(activity)
        initInjector()
    }

    @Inject
    lateinit var useCase: ShareExGetAffiliateEligibilityUseCase

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    lateinit var analytics: ShareExAnalytics

    private fun addObserver(activity: FragmentActivity) {
        activity.lifecycle.addObserver(this)
    }

    private fun initInjector() {
        val baseMainApplication = weakActivity.get()?.applicationContext as? BaseMainApplication
        baseMainApplication?.let {
            DaggerShareExComponent
                .builder()
                .baseAppComponent(it.baseAppComponent)
                .build()
                .inject(this)
        }
    }

    fun additionalCheck(arg: ShareExInitializerArg) {
        weakActivity.get()?.lifecycleScope?.launch {
            try {
                /**
                 * Add more checker and args here if needed
                 */
                when {
                    (arg.affiliateEligibilityRequest != null) -> {
                        observeAffiliateEligibility(
                            arg.affiliateEligibilityRequest,
                            arg.onSuccess,
                            arg.onError,
                            arg.onLoading
                        )
                    }
                    else -> Unit
                }
            } catch (throwable: Throwable) {
                arg.onError(throwable)
            }
        }
    }

    private suspend fun observeAffiliateEligibility(
        affiliateEligibilityRequest: ShareExAffiliateEligibilityRequest,
        onSuccess: (ShareExInitializationUiState) -> Unit,
        onError: (Throwable) -> Unit,
        onLoading: () -> Unit
    ) {
        useCase.getData(affiliateEligibilityRequest).collectLatest {
            when (it) {
                is ShareExResult.Success -> {
                    withContext(dispatchers.main) {
                        onSuccess(
                            ShareExInitializationUiState(
                                isEligibleAffiliate = it.data.isEligible
                            )
                        )
                    }
                }
                is ShareExResult.Error -> {
                    withContext(dispatchers.main) {
                        onError(it.throwable)
                    }
                }
                ShareExResult.Loading -> {
                    withContext(dispatchers.main) {
                        onLoading()
                    }
                }
            }
        }
    }

    fun openShareBottomSheet(
        bottomSheetArg: ShareExBottomSheetArg
    ) {
        this.bottomSheetArg = bottomSheetArg
        showLoadingDialog()
    }

    private fun showLoadingDialog() {
        weakActivity.get()?.let { activity ->
            bottomSheetArg?.let { arg ->
                dialog = ShareExLoadingDialog(
                    context = activity,
                    id = arg.identifier,
                    pageTypeEnum = arg.pageTypeEnum,
                    onResult = ::onResultShareBottomSheetModel
                ).also { dialog ->
                    dialog.show()
                }
            }
        }
    }

    private fun onResultShareBottomSheetModel(result: ShareExResult<ShareExBottomSheetModel>) {
        dialog?.dismiss()
        weakActivity.get()?.let {
            bottomSheetArg?.let { arg ->
                when (result) {
                    is ShareExResult.Success -> {
                        bottomSheetArg = arg.copy(
                            bottomSheetModel = result.data
                        )
                        showShareBottomSheet(it)
                    }
                    is ShareExResult.Error -> {
                        bottomSheetArg = arg.copy(
                            throwable = result.throwable
                        )
                        showShareBottomSheet(it)
                    }
                    ShareExResult.Loading -> Unit
                }
            }
        }
    }

    private fun showShareBottomSheet(fragmentActivity: FragmentActivity) {
        bottomSheetArg?.let {
            bottomSheet?.dismiss()
            bottomSheet = ShareExBottomSheet.newInstance(it)
            bottomSheet?.setListener(this)
            bottomSheet?.show(fragmentActivity.supportFragmentManager, "")
            trackClickIconShare()
        }
    }

    private fun trackClickIconShare() {
        bottomSheetArg?.let {
            analytics.trackActionClickIconShare(
                identifier = it.identifier,
                pageTypeEnum = it.pageTypeEnum,
                shareId = it.bottomSheetModel?.bottomSheetPage?.listShareProperty?.firstOrNull()?.shareId.toString(),
                label = it.trackerArg.labelActionClickShareIcon
            )
        }
    }

    private fun showToaster(
        view: View,
        text: String,
        type: Int,
        duration: Int,
        actionText: String = "",
        clickListener: View.OnClickListener? = null
    ) {
        if (actionText.isNotBlank() && clickListener != null) {
            Toaster.build(
                view = view,
                text = text,
                duration = duration,
                type = type,
                actionText = actionText,
                clickListener = clickListener
            ).show()
        } else {
            Toaster.build(
                view = view,
                text = text,
                duration = duration,
                type = type
            ).show()
        }
    }

    override fun onSuccessCopyLink() {
        weakActivity.get()?.let {
            val contentView: View? = it.findViewById(android.R.id.content)
            contentView?.let { view ->
                showToaster(
                    view = view,
                    text = it.getString(R.string.shareex_success_copy_link),
                    duration = Toaster.LENGTH_SHORT,
                    type = Toaster.TYPE_NORMAL
                )
            }
        }
    }

    override fun refreshPage() {
        bottomSheet?.dismiss()
        showLoadingDialog()
    }

    override fun onFailGenerateAffiliateLink(shortLink: String) {
        weakActivity.get()?.let {
            val contentView: View? = it.findViewById(android.R.id.content)
            contentView?.let { view ->
                showToaster(
                    view = contentView,
                    text = it.getString(R.string.shareex_fail_generate_affiliate_link),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR,
                    actionText = it.getString(R.string.shareex_action_copy_link),
                    clickListener = {
                        val isSuccessCopy = view.context.copyTextToClipboard(shortLink)
                        if (isSuccessCopy) {
                            onSuccessCopyLink()
                        }
                    }
                )
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        dialog?.dismiss()
        dialog = null
        bottomSheet = null
        bottomSheetArg = null
    }
}
