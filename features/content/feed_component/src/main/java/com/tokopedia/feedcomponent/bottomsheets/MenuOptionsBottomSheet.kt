package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_menu_options.*

class MenuOptionsBottomSheet : BottomSheetUnify() {
    private var isReportable: Boolean = false
    private var canBeDeleted: Boolean = false
    private var canBeUnFollow: Boolean = false
    var onReport: (() -> Unit)? = null
    var onFollow: (() -> Unit)? = null
    var onDelete: (() -> Unit)? = null
    var onEdit: (() -> Unit)? = null
    var onDismiss: (() -> Unit)? = null
    var onClosedClicked: (() -> Unit)? = null
    private var dismissedByClosing = false

    companion object {
        fun newInstance(
            isReportable: Boolean = true,
            canUnfollow: Boolean = false,
            isDeletable: Boolean = true
        ): MenuOptionsBottomSheet {
            return MenuOptionsBottomSheet().apply {
                this.canBeUnFollow = canUnfollow
                this.isReportable = isReportable
                this.canBeDeleted = isDeletable
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = View.inflate(context, R.layout.bottomsheet_menu_options, null)
        clearContentPadding = true
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shouldShowNewContentCreationFlow = enableContentCreationNewFlow()
        report.showWithCondition(!canBeDeleted && isReportable)
        follow.showWithCondition(!canBeDeleted && canBeUnFollow)
        delete.showWithCondition(canBeDeleted)
        edit.showWithCondition(canBeDeleted && shouldShowNewContentCreationFlow)

        if (canBeDeleted && report.isVisible && follow.isVisible) {
            div0.show()
            div1.show()
            div2.show()
        } else {
            if (report.isVisible && follow.isVisible) {
                div1.show()
            }
            if (follow.isVisible && canBeDeleted) {
                div2.show()
                div0.show()
            }
            if (report.isVisible && canBeDeleted) {
                div2.show()
                div0.show()
            }
            if(edit.isVisible){
                div0.show()
            }
        }
        if(!edit.isVisible){
            div0.hide()
        }

        follow.setOnClickListener {
            dismissedByClosing = true
            onFollow?.invoke()
            dismiss()
        }
        report.setOnClickListener {
            dismissedByClosing = true
            onReport?.invoke()
            dismiss()
        }
        delete?.setOnClickListener {
            dismissedByClosing = true
            onDelete?.invoke()
            dismiss()
        }
        edit?.setOnClickListener {
            dismissedByClosing = true
            onEdit?.invoke()
            dismiss()
        }
        setCloseClickListener {
            dismissedByClosing = true
            onClosedClicked?.invoke()
            dismiss()
        }
        setOnDismissListener {
            if (!dismissedByClosing)
                onDismiss?.invoke()
        }
    }
    private fun enableContentCreationNewFlow(): Boolean {
        val config: RemoteConfig = FirebaseRemoteConfigImpl(context)
        return config.getBoolean(RemoteConfigKey.ENABLE_NEW_CONTENT_CREATION_FLOW, true)
    }
}