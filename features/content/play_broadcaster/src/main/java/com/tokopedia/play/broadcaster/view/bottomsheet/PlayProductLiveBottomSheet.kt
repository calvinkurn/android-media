package com.tokopedia.play.broadcaster.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.view.adapter.PlayProductLiveAdapter
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * Created by jegul on 11/06/20
 */
class PlayProductLiveBottomSheet @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val dialogCustomizer: PlayBroadcastDialogCustomizer
) : BottomSheetUnify() {

    private lateinit var clProductLive: ConstraintLayout
    private lateinit var rvProductLive: RecyclerView

    private val productLiveAdapter = PlayProductLiveAdapter()

    private lateinit var parentViewModel: PlayBroadcastViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeProductList()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupBottomSheet() {
        setChild(getContentView())
    }

    private fun getContentView(): View {
        val view = View.inflate(requireContext(), R.layout.bottom_sheet_product_live, null)
        clProductLive = view.findViewById(R.id.cl_product_live)
        rvProductLive = view.findViewById(R.id.rv_product_live)
        return view
    }

    private fun setupView(view: View) {
        clProductLive.layoutParams = clProductLive.layoutParams.apply {
            height = (getScreenHeight() * HEIGHT_MULTIPLIER).toInt()
        }

        rvProductLive.adapter = productLiveAdapter
        rvProductLive.addItemDecoration(PlayGridTwoItemDecoration(requireContext()))
    }

    private fun observeProductList() {
        parentViewModel.observableProductList.observe(viewLifecycleOwner) {
            bottomSheetTitle.text = getString(R.string.play_product_live_title, it.size)
            productLiveAdapter.setItemsAndAnimateChanges(it)
        }
    }

    companion object {

        private const val TAG = "PlayProductLiveBottomSheet"
        private const val HEIGHT_MULTIPLIER = 0.8f
    }
}