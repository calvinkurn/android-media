package com.tkpd.atc_variant.views.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tkpd.atc_variant.R
import com.tkpd.atc_variant.di.AtcVariantComponent
import com.tkpd.atc_variant.di.DaggerAtcVariantComponent
import com.tkpd.atc_variant.views.AtcVariantListener
import com.tkpd.atc_variant.views.AtcVariantSharedViewModel
import com.tkpd.atc_variant.views.AtcVariantViewModel
import com.tkpd.atc_variant.views.adapter.AtcVariantAdapter
import com.tkpd.atc_variant.views.adapter.AtcVariantAdapterTypeFactoryImpl
import com.tkpd.atc_variant.views.adapter.AtcVariantDiffutil
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Yehezkiel on 05/05/21
 */
class AtcVariantBottomSheet : BottomSheetUnify(), AtcVariantListener, HasComponent<AtcVariantComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AtcVariantViewModel::class.java)
    }

    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(AtcVariantSharedViewModel::class.java)
    }

    private val adapterFactory by lazy { AtcVariantAdapterTypeFactoryImpl(this) }
    private val adapter by lazy {
        val asyncDifferConfig = AsyncDifferConfig.Builder(AtcVariantDiffutil())
                .build()
        AtcVariantAdapter(asyncDifferConfig, adapterFactory)
    }

    private var currentData: List<AtcVariantVisitable> = listOf()
    private var listener: AtcVariantBottomSheetListener? = null
    private var rvVariantBottomSheet: RecyclerView? = null

    fun show(fragmentManager: FragmentManager,
             tag: String,
             listener: AtcVariantBottomSheetListener) {

        this.listener = listener
        show(fragmentManager, tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initLayout() {
        isHideable = true
        clearContentPadding = true

        setTitle(context?.getString(R.string.title_bottomsheet_atc_variant) ?: "")

        setShowListener {
            bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {
                }

                override fun onStateChanged(p0: View, p1: Int) {
                    if (p1 == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }

        setOnDismissListener {
            listener?.onBottomSheetDismiss()
        }

        val view = View.inflate(context, R.layout.bottomsheet_atc_variant, null)

        setupRv(view)
        setChild(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun setupRv(view: View) {
        rvVariantBottomSheet = view.findViewById(R.id.rv_atc_variant_bottomsheet)
        rvVariantBottomSheet?.adapter = adapter
    }

    private fun observeData() {
        sharedViewModel.aggregatorParams.observeOnce(viewLifecycleOwner,{
            viewModel.decideInitialValue(it)
        })

        viewModel.initialData.observe(viewLifecycleOwner, {
            if (it is Success) {
                currentData = it.data
                adapter.submitList(it.data)
            }
        })
    }

    override fun getComponent(): AtcVariantComponent {
        return DaggerAtcVariantComponent
                .builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun onVariantClicked(variantOptions: VariantOptionWithAttribute) {
        viewModel.onVariantClicked(variantOptions.variantCategoryKey, variantOptions.variantId, variantOptions.imageOriginal, variantOptions.level)
    }

    override fun getStockWording(): String {
        return ""
    }
}

interface AtcVariantBottomSheetListener {
    fun onBottomSheetDismiss()
}