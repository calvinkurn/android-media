package com.tokopedia.notifcenter.presentation.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.Ratio
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.widget.BroadcastBannerNotificationImageView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

open class NotificationLongerContentBottomSheet : BottomSheetUnify() {

    protected open var notification: NotificationUiModel? = null
    protected open var contentTitle: Typography? = null
    protected open var contentDesc: Typography? = null
    protected open var cta: UnifyButton? = null
    protected open var contentContainer: LinearLayout? = null
    protected open var coordinatorContainer: CoordinatorLayout? = null
    protected open var banner: BroadcastBannerNotificationImageView? = null

    fun showMessage(@StringRes stringRes: Int) {
        val msg = getString(stringRes)
        coordinatorContainer?.let {
            Toaster.build(it, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    fun showErrorMessage(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        coordinatorContainer?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
                    .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        initContentView()
        initViewConfig()
        return super.onCreateView(inflater, container, savedInstanceState)?.also {
            initViewBinding(it)
            onInitContentView()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContentDesc()
        initCtaButton()
        initTitle()
        initBanner()
    }

    private fun initBanner() {
        if (notification?.isBanner() == true) {
            val imageRatio = notification?.imageMetaData?.getOrNull(0)?.ratio ?: Ratio()
            banner?.show()
            banner?.ratio = (imageRatio.y / imageRatio.x)
            ImageHandler.loadImageRounded(
                    context,
                    banner,
                    notification?.dataNotification?.infoThumbnailUrl,
                    bannerRadius
            )
        } else {
            banner?.hide()
        }
    }

    protected open fun initContentDesc() {
        contentDesc?.text = notification?.shortDescription
    }

    protected open fun initCtaButton() {
        if (notification?.buttonText?.isNotEmpty() == true) {
            cta?.text = notification?.buttonText
        }
        notification?.dataNotification?.appLink?.let { applink ->
            cta?.setOnClickListener {
                RouteManager.route(it.context, applink)
                dismiss()
            }
        }
    }

    protected open fun initTitle() {
        contentTitle?.text = notification?.title
    }

    protected open fun parseArguments() {
        arguments?.getString(KEY_NOTIFICATION_PAYLOAD)?.let { notificationString ->
            notification = CommonUtils.fromJson(
                    notificationString, NotificationUiModel::class.java
            )
        }
    }

    protected open fun initViewBinding(view: View) {
        contentTitle = view.findViewById(R.id.tv_title_bs)
        contentDesc = view.findViewById(R.id.tv_content_desc)
        cta = view.findViewById(R.id.btn_longer_content_cta)
        contentContainer = view.findViewById(R.id.content_container)
        coordinatorContainer = view.findViewById(R.id.coordinator_container)
        banner = view.findViewById(R.id.content_image)
    }

    protected open fun initContentView() {
        val contentView = View.inflate(context, R.layout.bottom_sheet_longer_content, null)
        setChild(contentView)
    }

    protected open fun onInitContentView() {}

    protected open fun initViewConfig() {
        clearContentPadding = true
        isHideable = true
        isDragable = true
        setShowListener {
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheet.skipCollapsed = true
        }
    }

    companion object {
        const val KEY_NOTIFICATION_PAYLOAD = "notification_payload"

        private val bannerRadius = 8f.toPx()

        fun create(notification: NotificationUiModel): NotificationLongerContentBottomSheet {
            val notificationString = CommonUtils.toJson(notification)
            val bundle = Bundle().apply {
                putString(KEY_NOTIFICATION_PAYLOAD, notificationString)
            }
            return NotificationLongerContentBottomSheet().apply {
                arguments = bundle
            }
        }
    }
}