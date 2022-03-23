package com.tokopedia.tokofood.purchase.purchasepage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.LayoutFragmentPurchaseBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.adapter.TokoFoodPurchaseAdapter
import com.tokopedia.tokofood.purchase.purchasepage.view.adapter.TokoFoodPurchaseAdapterTypeFactory
import com.tokopedia.tokofood.purchase.purchasepage.view.di.DaggerTokoFoodPurchaseComponent
import com.tokopedia.tokofood.purchase.purchasepage.view.toolbar.TokoFoodPurchaseToolbar
import com.tokopedia.tokofood.purchase.purchasepage.view.toolbar.TokoFoodPurchaseToolbarListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseProductUiModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoFoodPurchaseFragment : BaseListFragment<Visitable<*>, TokoFoodPurchaseAdapterTypeFactory>(),
        TokoFoodPurchaseActionListener, TokoFoodPurchaseToolbarListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewBinding by autoClearedNullable<LayoutFragmentPurchaseBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodPurchaseViewModel::class.java)
    }

    private var toolbar: TokoFoodPurchaseToolbar? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var adapter: TokoFoodPurchaseAdapter

    companion object {
        const val HAS_ELEVATION = 6
        const val NO_ELEVATION = 0

        fun createInstance(): TokoFoodPurchaseFragment {
            return TokoFoodPurchaseFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = LayoutFragmentPurchaseBinding.inflate(inflater, container, false)
        val view = viewBinding?.root
        recyclerView = getRecyclerView(view)
        (recyclerView?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackground()
        initializeToolbar(view)
        initializeRecyclerViewScrollListener()
        observeList()
        observeUiEvent()
    }

    override fun onItemClicked(t: Visitable<*>?) {
        // No-op
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun getRecyclerViewResourceId() = R.id.recycler_view_purchase

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodPurchaseComponent
                    .builder()
                    .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun loadData(page: Int) {
        showLoading()
        viewModel.loadData()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, TokoFoodPurchaseAdapterTypeFactory> {
        adapter = TokoFoodPurchaseAdapter(adapterTypeFactory)
        return adapter
    }

    override fun getAdapterTypeFactory(): TokoFoodPurchaseAdapterTypeFactory {
        return TokoFoodPurchaseAdapterTypeFactory(this)
    }

    override fun onBackPressed() {
        activity?.finish()
    }

    private fun initializeToolbar(view: View) {
        activity?.let {
            viewBinding?.toolbarPurchase?.removeAllViews()
            toolbar = TokoFoodPurchaseToolbar(it).apply {
                listener = this@TokoFoodPurchaseFragment
            }
            toolbar?.let {
                viewBinding?.toolbarPurchase?.addView(toolbar)
                (activity as AppCompatActivity).setSupportActionBar(viewBinding?.toolbarPurchase)
            }

            setToolbarShadowVisibility(false)
        }
    }

    private fun setToolbarShadowVisibility(show: Boolean) {
        if (show) {
            viewBinding?.appBarLayout?.elevation = HAS_ELEVATION.toFloat()
        } else {
            viewBinding?.appBarLayout?.elevation = NO_ELEVATION.toFloat()
        }
    }

    private fun setBackground() {
        activity?.let {
            it.window.decorView.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
        }
    }

    private fun initializeRecyclerViewScrollListener() {
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // No-op
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.canScrollVertically(-1)) {
                    setToolbarShadowVisibility(true)
                } else {
                    setToolbarShadowVisibility(false)
                }
            }
        })
    }

    private fun observeList() {
        viewModel.visitables.observe(viewLifecycleOwner, {
            adapter.updateList(it)
        })
    }

    private fun observeUiEvent() {
        viewModel.uiEvent.observe(viewLifecycleOwner, {
            when (it.state) {
                UiEvent.STATE_REMOVE_ALL_PRODUCT -> navigateToMerchantPage()
            }
        })
    }

    private fun navigateToMerchantPage() {
        // Todo : navigate to merchant page
        activity?.finish()
    }

    override fun getNextItems(currentIndex: Int, count: Int): List<Visitable<*>> {
        return viewModel.getNextItems(currentIndex, count)
    }

    override fun onTextChangeShippingAddressClicked() {
        view?.let {
            Toaster.build(it, "onTextChangeShippingAddressClicked", Toaster.LENGTH_SHORT).show()
        }
    }

    override fun onTextSetPinpointClicked() {
        view?.let {
            Toaster.build(it, "onTextSetPinpointClicked", Toaster.LENGTH_SHORT).show()
        }
    }

    override fun onTextAddItemClicked() {
        view?.let {
            Toaster.build(it, "onTextAddItemClicked", Toaster.LENGTH_SHORT).show()
        }
    }

    override fun onTextBulkDeleteUnavailableProductsClicked() {
        viewModel.bulkDeleteUnavailableProducts()
    }

    override fun onQuantityChanged(newQuantity: Int) {
        view?.let {
            Toaster.build(it, "onQuantityChanged", Toaster.LENGTH_SHORT).show()
        }
    }

    override fun onIconDeleteProductClicked(element: TokoFoodPurchaseProductUiModel) {
        viewModel.deleteProduct(element.id)
    }

    override fun onTextChangeNotesClicked() {
        view?.let {
            Toaster.build(it, "onTextChangeNotesClicked", Toaster.LENGTH_SHORT).show()
        }
    }

    override fun onTextChangeNoteAndVariantClicked() {
        view?.let {
            Toaster.build(it, "onTextChangeNoteAndVariantClicked", Toaster.LENGTH_SHORT).show()
        }
    }

    override fun onToggleShowHideUnavailableItemsClicked() {
        viewModel.toggleUnavailableProductsAccordion()
    }

}