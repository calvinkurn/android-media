package com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.ui.ShareVoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.adapter.ShareVoucherAdapter
import kotlinx.android.synthetic.main.bottomsheet_mvc_share_voucher.view.*

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class ShareVoucherBottomSheet(
        private val parent: ViewGroup
) : BottomSheetUnify() {

    private var onItemClickListener: (Int) -> Unit = {}

    private val mAdapter by lazy {
        ShareVoucherAdapter {
            onItemClickListener(it.type)
            dismiss()
        }
    }

    init {
        val child = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_mvc_share_voucher, parent, false)

        setupView(child)
        setTitle(parent.context.getString(R.string.mvc_share))
        setChild(child)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    private fun setupView(view: View) = with(view) {
        rvMvcShareVoucher.run {
            layoutManager = LinearLayoutManager(view.context)
            adapter = mAdapter
        }
    }

    fun setOnItemClickListener(action: (Int) -> Unit): ShareVoucherBottomSheet {
        this.onItemClickListener = action
        return this
    }

    fun show(fm: FragmentManager) {
        mAdapter.clearAllElements()
        mAdapter.addElement(getSocmedList())
        showNow(fm, ShareVoucherBottomSheet::class.java.simpleName)
    }

    private fun getSocmedList(): List<ShareVoucherUiModel> {
        return listOf(
                ShareVoucherUiModel(R.drawable.ic_mvc_link, parent.context.getString(R.string.mvc_copy_link), SocmedType.COPY_LINK),
                ShareVoucherUiModel(R.drawable.ic_mvc_instagram, parent.context.getString(R.string.mvc_instagram), SocmedType.INSTAGRAM),
                ShareVoucherUiModel(R.drawable.ic_mvc_whatsapp, parent.context.getString(R.string.mvc_whatsapp), SocmedType.WHATSAPP),
                ShareVoucherUiModel(R.drawable.ic_mvc_line, parent.context.getString(R.string.mvc_line), SocmedType.LINE),
                ShareVoucherUiModel(R.drawable.ic_mvc_twitter, parent.context.getString(R.string.mvc_twitter), SocmedType.TWITTER),
                ShareVoucherUiModel(R.drawable.ic_mvc_lainnya, parent.context.getString(R.string.mvc_others), SocmedType.LAINNYA)
        )
    }
}