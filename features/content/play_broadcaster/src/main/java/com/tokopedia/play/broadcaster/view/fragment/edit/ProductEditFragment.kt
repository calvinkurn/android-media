package com.tokopedia.play.broadcaster.view.fragment.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
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

    @Inject
    lateinit var analytic: PlayBroadcastAnalytic

    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var viewModel: DataStoreViewModel

    private var mListener: SetupResultListener? = null

    private val editProductListener = object: SimpleEditProductBottomSheet.Listener {
        override fun onChooseOver(bottomSheet: BottomSheetDialogFragment) {
            openProductSetupBottomSheet()
            bottomSheet.dismiss()
            analytic.clickChooseOverOnEditProductBottomSheet()
        }

        override suspend fun onSaveEditedProductList(bottomSheet: BottomSheetDialogFragment, dataStore: PlayBroadcastSetupDataStore): Throwable? {
            analytic.clickSubmitOnEditProductBottomSheet()
            return mListener?.onSetupCompletedWithData(bottomSheet, dataStore)
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
        setupView(view, savedInstanceState)
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

    private fun setupView(view: View, savedInstanceState: Bundle?) {
        viewModel.setDataStore(parentViewModel.getCurrentSetupDataStore())

        if (savedInstanceState == null) openEditProductBottomSheet()
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
        return getFragmentByClassName(SimpleEditProductBottomSheet::class.java) as SimpleEditProductBottomSheet
    }

    private fun getProductSetupBottomSheet(): ProductSetupBottomSheet {
        return getFragmentByClassName(ProductSetupBottomSheet::class.java) as ProductSetupBottomSheet
    }
}