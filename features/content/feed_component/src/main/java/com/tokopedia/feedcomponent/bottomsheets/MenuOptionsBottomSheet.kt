package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.R
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_menu_options.*

class MenuOptionsBottomSheet : BottomSheetUnify() {
    private var isReportable: Boolean = false
    private var canBeDeleted: Boolean = false
    private var canBeUnFollow: Boolean = false
    var onReport: (() -> Unit)? = null
    var onFollow: (() -> Unit)? = null
    var onDelete: (() -> Unit)? = null
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
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delete.showWithCondition(canBeDeleted)
        report.showWithCondition(isReportable)
        follow.showWithCondition(canBeUnFollow)
        follow.setOnClickListener {
            onFollow?.invoke()
            dismiss()
        }
        report.setOnClickListener {
            onReport?.invoke()
            dismiss()
        }
        delete?.setOnClickListener {
            onDelete?.invoke()
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
}