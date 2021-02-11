package com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.ui.ShareVoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.adapter.ShareVoucherAdapter
import kotlinx.android.synthetic.main.bottomsheet_mvc_share_voucher.view.*

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class ShareVoucherBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(): ShareVoucherBottomSheet = ShareVoucherBottomSheet().apply {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        }

        const val TAG = "ShareVoucherBottomSheet"
    }

    private var broadCastChatStatus = 0

    private var broadCastChatQuota = 0

    private var onItemClickListener: (Int) -> Unit = {}

    private val mAdapter by lazy {
        ShareVoucherAdapter {
            onItemClickListener(it.type)
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    fun setBroadCastChatStatus(status: Int) {
        this.broadCastChatStatus = status
    }

    fun setBroadCastChatQuota(quota: Int) {
        this.broadCastChatQuota = quota
    }

    private fun initBottomSheet() {
        context?.run {
            val child = View.inflate(this, R.layout.bottomsheet_mvc_share_voucher, null)
            setTitle(getString(R.string.mvc_share))
            setChild(child)
        }
    }

    private fun setupView(view: View) = with(view) {
        mAdapter.clearAllElements()
        mAdapter.addElement(getSocmedList())
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
        showNow(fm, TAG)
    }

    private fun getSocmedList(): List<ShareVoucherUiModel> {
        val socmedList = mutableListOf<ShareVoucherUiModel>()
        socmedList.add(
                ShareVoucherUiModel(
                        R.drawable.ic_mvc_broadcast_chat,
                        context?.getString(R.string.mvc_broadcast_chat_tkpd).toBlankOrString(),
                        SocmedType.BROADCAST,
                        broadCastChatStatus,
                        broadCastChatQuota
                )
        )
        socmedList.addAll(
                listOf(
                        ShareVoucherUiModel(R.drawable.ic_mvc_link, context?.getString(R.string.mvc_copy_link).toBlankOrString(), SocmedType.COPY_LINK),
                        ShareVoucherUiModel(R.drawable.ic_mvc_instagram, context?.getString(R.string.mvc_instagram).toBlankOrString(), SocmedType.INSTAGRAM),
                        ShareVoucherUiModel(R.drawable.ic_mvc_facebook, context?.getString(R.string.mvc_facebook).toBlankOrString(), SocmedType.FACEBOOK),
                        ShareVoucherUiModel(R.drawable.ic_mvc_whatsapp, context?.getString(R.string.mvc_whatsapp).toBlankOrString(), SocmedType.WHATSAPP),
                        ShareVoucherUiModel(R.drawable.ic_mvc_line, context?.getString(R.string.mvc_line).toBlankOrString(), SocmedType.LINE),
                        ShareVoucherUiModel(R.drawable.ic_mvc_twitter, context?.getString(R.string.mvc_twitter).toBlankOrString(), SocmedType.TWITTER),
                        ShareVoucherUiModel(R.drawable.ic_mvc_lainnya, context?.getString(R.string.mvc_others).toBlankOrString(), SocmedType.LAINNYA)
                )
        )
        return socmedList
    }
}