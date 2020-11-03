package com.tokopedia.notifcenter.presentation.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class NotificationLongerContentBottomSheet : BottomSheetUnify() {

    private var notification: NotificationUiModel? = null
    private var contentDesc: Typography? = null
    private var cta: UnifyButton? = null

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
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContentDesc()
        initCtaButton()
        initTitle()
    }

    private fun initContentDesc() {
        contentDesc?.text = notification?.shortDescription
    }

    private fun initCtaButton() {
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

    private fun initTitle() {
        notification?.title?.let {
            setTitle(it)
        }
    }

    private fun parseArguments() {
        arguments?.getString(KEY_NOTIFICATION_PAYLOAD)?.let { notificationString ->
            notification = CommonUtils.fromJson(
                    notificationString, NotificationUiModel::class.java
            )
        }
    }

    private fun initViewBinding(view: View) {
        contentDesc = view.findViewById(R.id.tv_content_desc)
        cta = view.findViewById(R.id.btn_longer_content_cta)
    }

    private fun initContentView() {
        val contentView = View.inflate(context, R.layout.bottom_sheet_longer_content, null)
        setChild(contentView)
    }

    private fun initViewConfig() {
        clearContentPadding = true
    }

    companion object {
        private const val KEY_NOTIFICATION_PAYLOAD = "notification_payload"

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