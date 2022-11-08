package com.tokopedia.mvc.presentation.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.seller_mvc_creation.databinding.SmvcFragmentVoucherDetailBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class VoucherDetailFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(couponId: Long): VoucherDetailFragment {
            return VoucherDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }

    private var binding by autoClearedNullable<SmvcFragmentVoucherDetailBinding>()


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(VoucherDetailViewModel::class.java) }

    override fun getScreenName(): String =
        VoucherDetailFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentVoucherDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}
