package com.tokopedia.shopdiscount.bulk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.BottomsheetDiscountBulkApplyBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject


class DiscountBulkApplyBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_MODE = "mode"

        @JvmStatic
        fun newInstance(mode: Mode): DiscountBulkApplyBottomSheet {
            val args = Bundle()
            args.putSerializable(BUNDLE_KEY_MODE, mode)

            val bottomSheet = DiscountBulkApplyBottomSheet().apply {
                arguments = args
            }
            return bottomSheet
        }

    }

    private val mode by lazy { arguments?.getSerializable(BUNDLE_KEY_MODE) }

    private var binding by autoClearedNullable<BottomsheetDiscountBulkApplyBinding>()


    enum class Mode {
        SHOW_ALL_FIELDS,
        HIDE_PERIOD_FIELDS
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(DiscountBulkApplyViewModel::class.java) }

    private var onApplyClickListener: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()

    }

    private fun setupDependencyInjection() {
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
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BottomsheetDiscountBulkApplyBinding.inflate(inflater, container, false)

        isKeyboardOverlap = false
        clearContentPadding = true
        setChild(binding?.root)
        setTitle(getString(R.string.sd_bulk_manage))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeCouponDetail()
    }

    private fun observeCouponDetail() {

    }


    private fun setupView() {
        binding.run {

        }
    }


    private fun observeStartDateChange() {
        viewModel.startDate.observe(viewLifecycleOwner) { startDate ->

        }
    }

    private fun observeEndDateChange() {

    }


    fun setOnApplyClickListener(onApplyClickListener: () -> Unit) {
        this.onApplyClickListener = onApplyClickListener
    }
}
