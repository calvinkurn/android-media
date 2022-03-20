package com.tokopedia.tokofood.purchase.purchasepage.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.FragmentPurchaseBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapter
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory
import com.tokopedia.tokofood.purchase.purchasepage.presentation.di.TokoFoodPurchaseComponent
import com.tokopedia.tokofood.purchase.purchasepage.presentation.toolbar.TokoFoodPurchaseToolbar
import com.tokopedia.tokofood.purchase.purchasepage.presentation.toolbar.TokoFoodPurchaseToolbarListener
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoFoodPurchaseFragment : BaseListFragment<Visitable<*>, TokoFoodPurchaseAdapterTypeFactory>(),
        TokoFoodPurchaseActionListener, TokoFoodPurchaseToolbarListener {

    private var viewBinding by autoClearedNullable<FragmentPurchaseBinding>()
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
        viewBinding = FragmentPurchaseBinding.inflate(inflater, container, false)
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
    }

    override fun onItemClicked(t: Visitable<*>?) {
        // No-op
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun getRecyclerViewResourceId() = R.id.recycler_view_purchase

    override fun initInjector() {
        getComponent(TokoFoodPurchaseComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {

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

}