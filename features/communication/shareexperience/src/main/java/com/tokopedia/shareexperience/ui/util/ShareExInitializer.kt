package com.tokopedia.shareexperience.ui.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.data.analytic.ShareExAnalytics
import com.tokopedia.shareexperience.data.di.component.ShareExComponentFactoryProvider
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.request.affiliate.ShareExAffiliateEligibilityRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetAffiliateEligibilityUseCase
import com.tokopedia.shareexperience.domain.util.ShareExLogger
import com.tokopedia.shareexperience.domain.util.ShareExResult
import com.tokopedia.shareexperience.ui.DismissListener
import com.tokopedia.shareexperience.ui.ShareExBottomSheet
import com.tokopedia.shareexperience.ui.listener.ShareExBottomSheetListener
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetResultArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExInitializerArg
import com.tokopedia.shareexperience.ui.uistate.ShareExInitializationUiState
import com.tokopedia.shareexperience.ui.view.ShareExLoadingDialog
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
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
    context: Context
) : DefaultLifecycleObserver, ShareExBottomSheetListener {

    private val weakContext = WeakReference(context)
    private var dialog: ShareExLoadingDialog? = null
    private var bottomSheet: ShareExBottomSheet? = null
    private var bottomSheetArg: ShareExBottomSheetArg? = null
    var dismissListener: DismissListener? = null

    init {
        addObserver(context)
        initInjector()
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var useCase: ShareExGetAffiliateEligibilityUseCase

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    lateinit var analytics: ShareExAnalytics

    private fun addObserver(context: Context) {
        context.asLifecycleOwner()?.lifecycle?.addObserver(this)
    }

    private fun Context.asLifecycleOwner(): LifecycleOwner? {
        return when (this) {
            is LifecycleOwner -> this
            is ContextWrapper -> this.baseContext.asLifecycleOwner()
            else -> null
        }
    }

    private fun initInjector() {
        val application = weakContext.get()?.applicationContext as? Application
        application?.let {
            ShareExComponentFactoryProvider
                .instance
                .createShareExComponent(it)
                .inject(this)
        }
    }

    fun additionalCheck(arg: ShareExInitializerArg) {
        weakContext.get()?.let {
            val lifecycleScope = (it as? LifecycleOwner)?.lifecycleScope
            lifecycleScope?.launch {
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
                    ShareExLogger.logExceptionToServerLogger(
                        throwable = throwable,
                        deviceId = userSession.deviceId,
                        description = ::additionalCheck.name
                    )
                }
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
        weakContext.get()?.let { context ->
            bottomSheetArg?.let { arg ->
                dialog = ShareExLoadingDialog(
                    context = context,
                    arg = arg,
                    onResult = ::onResultShareBottomSheetModel
                ).also { dialog ->
                    dialog.show()
                }
            }
        }
    }

    private fun onResultShareBottomSheetModel(result: ShareExResult<ShareExBottomSheetModel>) {
        dialog?.dismiss()
        weakContext.get()?.let { context ->
            val fragmentManager = (context as? FragmentActivity)?.supportFragmentManager
            fragmentManager?.let { fm ->
                when (result) {
                    is ShareExResult.Success -> {
                        val resultArg = ShareExBottomSheetResultArg(
                            bottomSheetModel = result.data
                        )
                        showShareBottomSheet(fm, resultArg)
                    }
                    is ShareExResult.Error -> {
                        val resultArg = ShareExBottomSheetResultArg(
                            throwable = result.throwable
                        )
                        showShareBottomSheet(fm, resultArg)
                    }
                    ShareExResult.Loading -> Unit
                }
            }
        }
    }

    private fun showShareBottomSheet(
        fragmentManager: FragmentManager,
        result: ShareExBottomSheetResultArg
    ) {
        bottomSheetArg?.let {
            bottomSheet?.dismiss()
            bottomSheet = ShareExBottomSheet.newInstance(it, result)
            bottomSheet?.setListener(this)
            bottomSheet?.show(fragmentManager, TAG)
            bottomSheet?.setOnDismissListener {
                dismissListener?.onDismiss()
            }
            trackClickIconShare(result)
        }
    }

    private fun trackClickIconShare(result: ShareExBottomSheetResultArg) {
        bottomSheetArg?.let {
            analytics.trackActionClickIconShare(
                productId = it.productId,
                shopId = it.shopId,
                pageTypeEnum = it.pageTypeEnum,
                shareId = result.bottomSheetModel?.bottomSheetPage?.listShareProperty?.firstOrNull()?.shareId.toString(),
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
        dismissListener?.onSuccessCopyLink()
        weakContext.get()?.let { context ->
            val contentView: View? = (context as? Activity)?.findViewById(android.R.id.content)
            contentView?.let { view ->
                showToaster(
                    view = view,
                    text = context.getString(R.string.shareex_success_copy_link),
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
        dismissListener?.onFailGenerateAffiliateLink()
        weakContext.get()?.let { context ->
            val contentView: View? = (context as? Activity)?.findViewById(android.R.id.content)
            contentView?.let { view ->
                showToaster(
                    view = contentView,
                    text = context.getString(R.string.shareex_fail_generate_affiliate_link),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR,
                    actionText = context.getString(R.string.shareex_action_copy_link),
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

    companion object {
        private const val TAG = "ShareExperience"
    }
}
