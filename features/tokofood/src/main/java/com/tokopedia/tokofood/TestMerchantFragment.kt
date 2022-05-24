package com.tokopedia.tokofood

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.databinding.FragmentTokofoodMerchantTestBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductTokoFoodPurchaseUiModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class TestMerchantFragment: BaseMultiFragment() {

    companion object {
        fun createInstance(): TestMerchantFragment {
            return TestMerchantFragment()
        }
    }

    private var binding by autoClearedNullable<FragmentTokofoodMerchantTestBinding>()

    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null

    private val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()

    private val _updateQuantityState: MutableStateFlow<Int?> =
        MutableStateFlow(null)

    override fun getFragmentToolbar(): Toolbar? = null

    override fun getFragmentTitle(): String? = null

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokofoodMerchantTestBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectValue()
        setupView()
        setupDebounceTest()
    }

    override fun onResume() {
        super.onResume()
        initializeMiniCartWidget()
    }

    private fun setupDebounceTest() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            _updateQuantityState
                .debounce(1000)
                .flatMapConcat { count ->
                    flow {
                        if (count != null) {
                            emit(count)
                        }
                    }
                }
                .collect {
                    activityViewModel?.testUpdateCart()
                }
        }
    }

    private fun setupView() {
        binding?.run {
            testQuantity.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val quantity = s.toString().toIntOrZero()
                    _updateQuantityState.value = quantity
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            testMiniCart.setOnATCClickListener {
                goToPurchasePage()
            }

            testDelete.setOnClickListener {
                activityViewModel?.deleteProduct("", "", "tes")
            }

            testAdd.setOnClickListener {
                activityViewModel?.addToCart(UpdateParam(), "")
            }
        }
    }

    private fun collectValue() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel?.cartDataValidationFlow?.collect { uiEvent ->
                when(uiEvent.state) {
                    UiEvent.EVENT_SUCCESS_VALIDATE_CHECKOUT -> {
                        goToPurchasePage()
                    }
//                    UiEvent.EVENT_SUCCESS_DELETE_PRODUCT -> {
//                        view?.let {
//                            Toaster.build(it, "success delete", Toaster.LENGTH_SHORT).show()
//                        }
//                    }
//                    UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY -> {
//                        view?.let {
//                            Toaster.build(it, "success update quantity", Toaster.LENGTH_SHORT).show()
//                        }
//                    }
//                    UiEvent.EVENT_SUCCESS_ADD_TO_CART -> {
//                        view?.let {
//                            Toaster.build(it, "success atc", Toaster.LENGTH_SHORT).show()
//                        }
//                    }
                }
            }
        }
    }

    private fun initializeMiniCartWidget() {
        activityViewModel?.let {
            binding?.testMiniCart?.initialize(it, viewLifecycleOwner.lifecycleScope, "tes")
        }
    }

    private fun goToPurchasePage() {
        navigateToNewFragment(TokoFoodPurchaseFragment.createInstance())
    }

}