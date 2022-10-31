package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.BottomSheetProductManageNotifyMeBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class NotifyMeBuyerInformationBottomSheet : BottomSheetUnify() {

    var onClickEditStock: (() -> Unit)? = null

    companion object {
        private val TAG: String = NotifyMeBuyerInformationBottomSheet::class.java.simpleName
        private const val KEY_WORDING_NOTIFY_ME = "wordingNotifyMe"

        fun createInstance(wordingNotifyMe: String): NotifyMeBuyerInformationBottomSheet {
            return NotifyMeBuyerInformationBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(KEY_WORDING_NOTIFY_ME, wordingNotifyMe)
                }
                clearContentPadding = true
            }
        }
    }

    private var binding by autoClearedNullable<BottomSheetProductManageNotifyMeBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetProductManageNotifyMeBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, com.tokopedia.product.manage.common.R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()
                ?.remove(this@NotifyMeBuyerInformationBottomSheet)?.commit()
        }
    }

    fun show(
        fm: FragmentManager,
    ) {
        show(fm, TAG)
    }

    fun setOnClickEditProductStock(onClickEditStock: () -> Unit) {
        this.onClickEditStock = onClickEditStock
    }

    private fun setupView() {
        val title = context?.getString(R.string.product_manage_notify_me_title).orEmpty()
        setTitle(title)
        val wordingNotifyMe = arguments?.getString(KEY_WORDING_NOTIFY_ME).orEmpty()
        val description = wordingNotifyMe.parseAsHtml()
        binding?.tvMessage?.text = description
        binding?.btnManageStock?.setOnClickListener {
            onClickEditStock?.invoke()
            dismiss()
        }
    }
}
