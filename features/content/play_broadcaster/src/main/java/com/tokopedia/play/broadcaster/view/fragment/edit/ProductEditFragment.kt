package com.tokopedia.play.broadcaster.view.fragment.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.di.provider.PlayBroadcastComponentProvider
import com.tokopedia.play.broadcaster.di.setup.DaggerPlayBroadcastSetupComponent
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import javax.inject.Inject

/**
 * Created by jegul on 23/06/20
 */
class ProductEditFragment : TkpdBaseV4Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var simpleEditProductBottomSheet: SimpleEditProductBottomSheet
    private lateinit var productSetupBottomSheet: ProductSetupBottomSheet

    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var viewModel: DataStoreViewModel

    private var mListener: SetupResultListener? = null

    private val editProductListener = object: SimpleEditProductBottomSheet.Listener {
        override fun onChooseOver() {
            openProductSetupBottomSheet()
            getSimpleEditProductBottomSheet().dismiss()
        }

        override suspend fun onSaveEditedProductList(dataStore: PlayBroadcastSetupDataStore): Throwable? {
            return mListener?.onSetupCompletedWithData(simpleEditProductBottomSheet, dataStore)
        }
    }

    override fun getScreenName(): String = "Product Edit Fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        childFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DataStoreViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_empty_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        when (childFragment) {
            is ProductSetupBottomSheet -> {
                mListener?.let { childFragment.setListener(it) }
            }
            is SimpleEditProductBottomSheet -> {
                childFragment.setListener(editProductListener)
            }
        }
    }

    fun setListener(listener: SetupResultListener) {
        mListener = listener
    }

    private fun inject() {
        DaggerPlayBroadcastSetupComponent.builder()
                .setBroadcastComponent((requireActivity() as PlayBroadcastComponentProvider).getBroadcastComponent())
                .build()
                .inject(this)
    }

    private fun initView(view: View) {
    }

    private fun setupView(view: View) {
        viewModel.setDataStore(parentViewModel.getCurrentSetupDataStore())

        openEditProductBottomSheet()
    }

    private fun openProductSetupBottomSheet() {
        getProductSetupBottomSheet()
                .show(childFragmentManager)
    }

    private fun openEditProductBottomSheet() {
        getSimpleEditProductBottomSheet()
                .show(childFragmentManager)
    }

    private fun getFragmentByClassName(fragmentClass: Class<out Fragment>): Fragment {
        return fragmentFactory.instantiate(requireContext().classLoader, fragmentClass.name)
    }

    private fun getSimpleEditProductBottomSheet(): SimpleEditProductBottomSheet {
        if (!::simpleEditProductBottomSheet.isInitialized) {
            simpleEditProductBottomSheet =
                    getFragmentByClassName(SimpleEditProductBottomSheet::class.java) as SimpleEditProductBottomSheet
        }
        return simpleEditProductBottomSheet
    }

    private fun getProductSetupBottomSheet(): ProductSetupBottomSheet {
        if (!::productSetupBottomSheet.isInitialized) {
            productSetupBottomSheet =
                    getFragmentByClassName(ProductSetupBottomSheet::class.java) as ProductSetupBottomSheet
        }
        return productSetupBottomSheet
    }
}