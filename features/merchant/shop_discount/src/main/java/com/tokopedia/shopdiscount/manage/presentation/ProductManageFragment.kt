package com.tokopedia.shopdiscount.manage.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.shopdiscount.bulk.presentation.DiscountBulkApplyBottomSheet
import com.tokopedia.shopdiscount.databinding.FragmentProductManageBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable


class ProductManageFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = ProductManageFragment().apply {
            arguments = Bundle()
        }
    }

    private var binding by autoClearedNullable<FragmentProductManageBinding>()
    override fun getScreenName() : String = ProductManageFragment::class.java.canonicalName.orEmpty()
    override fun initInjector() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductManageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        displayBulkApplyBottomSheet()
    }

    private fun setupViews() {
        binding?.run {

        }
    }


    private fun displayBulkApplyBottomSheet() {
        val bottomSheet = DiscountBulkApplyBottomSheet.newInstance()
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }
}