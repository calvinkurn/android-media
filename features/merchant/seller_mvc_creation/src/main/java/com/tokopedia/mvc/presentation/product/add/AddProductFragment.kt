package com.tokopedia.mvc.presentation.product.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.seller_mvc_creation.databinding.SmvcFragmentAddProductBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject


class AddProductFragment : BaseDaggerFragment() {

    companion object {

        @JvmStatic
        fun newInstance(couponId: Long): AddProductFragment {
            return AddProductFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }

    }

    private var binding by autoClearedNullable<SmvcFragmentAddProductBinding>()


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(AddProductViewModel::class.java) }


    override fun getScreenName(): String =
        AddProductFragment::class.java.canonicalName.orEmpty()

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
        binding = SmvcFragmentAddProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeValidationResult()

        //viewModel.getCurrentMonthRemainingQuota()
    }

    private fun observeValidationResult() {
        /* viewModel.areInputValid.observe(viewLifecycleOwner) { validationResult ->
             handleValidationResult(validationResult)
         }*/
    }


}
