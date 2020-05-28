package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder
import com.tokopedia.play.broadcaster.view.adapter.PlayEtalaseDetailAdapter
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * Created by jegul on 27/05/20
 */
class PlayEtalaseDetailFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseSetupFragment() {

    private lateinit var viewModel: PlayEtalasePickerViewModel

    private lateinit var tvInfo: TextView
    private lateinit var rvProduct: RecyclerView

    private val etalaseDetailAdapter = PlayEtalaseDetailAdapter(object : ProductSelectableViewHolder.Listener {
        override fun onProductSelectStateChanged(productId: Long, isSelected: Boolean) {
            viewModel.selectProduct(productId, isSelected)
        }

        override fun onProductSelectError(reason: Throwable) {
            //TODO("Increase distance from bottom")
            Toaster.make(
                    view = requireView(),
                    text = reason.localizedMessage,
                    duration = Toaster.LENGTH_SHORT,
                    actionText = getString(R.string.play_ok)
            )
        }
    })

    override fun getTitle(): String {
        return ""
    }

    override fun isRootFragment(): Boolean {
        return false
    }

    override fun getScreenName(): String = "Etalase Detail"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
        arguments?.getLong(EXTRA_ETALASE_ID)?.let { etalaseId -> viewModel.setSelectedEtalase(etalaseId) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_etalase_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeProductsInSelectedEtalase()
    }

    private fun initView(view: View) {
        with(view) {
            tvInfo = findViewById(R.id.tv_info)
            rvProduct = findViewById(R.id.rv_product)
        }
    }

    private fun setupView(view: View) {
        rvProduct.adapter = etalaseDetailAdapter
        rvProduct.addItemDecoration(PlayGridTwoItemDecoration(requireContext()))
    }

    private fun observeProductsInSelectedEtalase() {
        viewModel.observableSelectedEtalase.observe(viewLifecycleOwner, Observer {
            etalaseDetailAdapter.setItemsAndAnimateChanges(it.productList)
            broadcastCoordinator.setupTitle(it.name)
            tvInfo.text = getString(R.string.play_product_select_max_info, viewModel.maxProduct)
        })
    }

    companion object {

        const val EXTRA_ETALASE_ID = "etalase_id"
    }
}