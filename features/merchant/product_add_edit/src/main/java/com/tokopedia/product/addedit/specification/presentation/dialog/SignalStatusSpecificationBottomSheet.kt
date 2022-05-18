package com.tokopedia.product.addedit.specification.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_SIGNAL_STATUS_SPECIFICATION
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class SignalStatusSpecificationBottomSheet: BottomSheetUnify() {

    companion object {
        const val TAG = "Signal Status Specification Bottom Sheet"
    }

    private var ivSignalStatus: AppCompatImageView? = null
    private var tvTipsText1: Typography? = null
    private var tvTipsText2: Typography? = null

    init {
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle(getString(R.string.label_specification_signal_status_bottomsheet))
        setupImageView()
    }

    private fun setupImageView() {
        ivSignalStatus?.loadImage(PHOTO_SIGNAL_STATUS_SPECIFICATION)
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }

    private fun initChildLayout() {
        overlayClickDismiss = true
        val contentView: View? = View.inflate(context,
                R.layout.add_edit_product_specification_signal_status_bottom_sheet_content, null)
        ivSignalStatus = contentView?.findViewById(R.id.ivSignalStatus)
        tvTipsText1 = contentView?.findViewById(R.id.tvTipsText1)
        tvTipsText2 = contentView?.findViewById(R.id.tvTipsText2)
        tvTipsText1?.text = MethodChecker.fromHtml(getString(R.string.label_specification_signal_status_tips1))
        tvTipsText2?.text = MethodChecker.fromHtml(getString(R.string.label_specification_signal_status_tips2))
        setChild(contentView)
    }
}