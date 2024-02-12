package com.tokopedia.tokopedianow.shoppinglist.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.BottomSheetUtil.configureMaxHeight
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.ShoppingListOtherProductsAdapterTypeFactory
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowShoppingListOtherProductsBinding
import com.tokopedia.tokopedianow.shoppinglist.di.component.DaggerShoppingListComponent
import com.tokopedia.tokopedianow.shoppinglist.di.component.ShoppingListComponent
import com.tokopedia.tokopedianow.shoppinglist.di.module.ShoppingListModule
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.VisitableMapper.addRecommendationProducts
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.ShoppingListOtherProductsAdapter
import com.tokopedia.tokopedianow.shoppinglist.presentation.decoration.ShoppingListDecoration
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ShoppingListOtherProductsBottomSheet
    : BottomSheetUnify(), HasComponent<ShoppingListComponent> {

    /**
     * -- lateinit variable section --
     */

//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory
//
//    private val viewModel by lazy {
//        ViewModelProvider(this, viewModelFactory)[ShoppingListOtherProductsBottomSheetViewModel::class.java]
//    }

    private var binding by autoClearedNullable<BottomsheetTokopedianowShoppingListOtherProductsBinding>()

    private val adapter by lazy {
        ShoppingListOtherProductsAdapter(
            ShoppingListOtherProductsAdapterTypeFactory()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getComponent(): ShoppingListComponent = DaggerShoppingListComponent.builder()
        .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
        .shoppingListModule(ShoppingListModule(requireContext()))
        .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureMaxHeight(false)
//        observeLiveData()
//
        setupRecyclerView()
//
//        viewModel.loadLayout()
        layout.addRecommendationProducts()
        adapter.submitList(layout)
    }

    private val layout = mutableListOf<Visitable<*>>()


    private fun initLayout() {
        binding = BottomsheetTokopedianowShoppingListOtherProductsBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        configureBottomSheet()
    }

//    private fun observeLiveData() {
//        viewModel.productRecommendation.observe(viewLifecycleOwner) {
//            adapter.submitList(it)
//        }
//    }

    private fun configureBottomSheet() {
        setTitle(getString(R.string.tokopedianow_shopping_list_other_options))
        clearContentPadding = true
        showCloseIcon = true
        isHideable = true
        setCloseClickListener {
            dismiss()
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = this@ShoppingListOtherProductsBottomSheet.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ShoppingListDecoration())
        }
    }
}
