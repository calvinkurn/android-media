package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.product.manage.databinding.BottomSheetProductManageMoreMenuBinding
import com.tokopedia.product.manage.feature.list.view.adapter.ProductManageMoreMenuAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductManageMoreMenuViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ProductManageMoreMenuBottomSheet(
        context: Context? = null,
        listener: ProductManageMoreMenuViewHolder.ProductManageMoreMenuListener? = null,
        private val fm: FragmentManager? = null
): BottomSheetUnify() {

    companion object {
        private val TAG = ProductManageMoreMenuBottomSheet::class.java.simpleName
    }

    private var moreMenuAdapter: ProductManageMoreMenuAdapter? = null

    private var binding by autoClearedNullable<BottomSheetProductManageMoreMenuBinding>()

    init {
        if (context != null && listener != null && fm != null) {
            moreMenuAdapter = ProductManageMoreMenuAdapter(context, listener)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetProductManageMoreMenuBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()?.remove(this@ProductManageMoreMenuBottomSheet)?.commit()
        }
    }

    fun show() {
        fm?.let {
            show(it, TAG)
        }
    }

    private fun setupView() {
        binding?.moreMenuList?.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = moreMenuAdapter
        }
    }

}