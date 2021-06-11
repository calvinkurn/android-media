package com.tokopedia.play.broadcaster.view.fragment.edit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.di.provider.PlayBroadcastComponentProvider
import com.tokopedia.play.broadcaster.di.setup.DaggerPlayBroadcastSetupComponent
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.util.helper.CoverImagePickerHelper
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayCoverSetupViewModel
import javax.inject.Inject

/**
 * Created by jegul on 22/06/20
 */
class CoverEditFragment : TkpdBaseV4Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var analytic: PlayBroadcastAnalytic

    private lateinit var imagePickerHelper: CoverImagePickerHelper

    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel
    private lateinit var viewModel: PlayCoverSetupViewModel

    private var mListener: SetupResultListener? = null

    private val coverEditListener = object : CoverCropEditBottomSheet.EditCoverResultListener {
        override fun onChangeCoverFromCropping(coverSource: CoverSource) {
            getImagePickerHelper()
                    .show(coverSource)
        }

        override fun onSetupCanceled() {
            mListener?.onSetupCanceled()
        }

        override suspend fun onSetupCompletedWithData(bottomSheet: BottomSheetDialogFragment, dataStore: PlayBroadcastSetupDataStore): Throwable? {
            return mListener?.onSetupCompletedWithData(bottomSheet, dataStore)
        }
    }

    override fun getScreenName(): String = "Cover Edit Fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        childFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayCoverSetupViewModel::class.java)
        dataStoreViewModel = ViewModelProvider(this, viewModelFactory).get(DataStoreViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_empty_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!getImagePickerHelper().onActivityResult(requestCode, resultCode, data)) {
            (activity as? PlayBroadcastActivity)?.startPreview()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        if (childFragment is CoverCropEditBottomSheet) {
            childFragment.setListener(coverEditListener)
        }

        getImagePickerHelper().onAttachFragment(childFragment)
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
        dataStoreViewModel.setDataStore(parentViewModel.getCurrentSetupDataStore())

        if (savedInstanceState == null) {
            getImagePickerHelper()
                    .show()
        }
    }

    private fun getImagePickerHelper(): CoverImagePickerHelper {
        if (!::imagePickerHelper.isInitialized) {
            imagePickerHelper = CoverImagePickerHelper(
                    context = requireContext(),
                    fragmentManager = childFragmentManager,
                    listener = object : CoverImagePickerHelper.OnChosenListener {
                        override fun onGetFromProduct(productId: Long, imageUrl: String) {
                            viewModel.setCroppingProductCover(productId, imageUrl)
                            openCoverCropEditFragment()
                        }

                        override fun onGetFromCamera(uri: Uri) {
                            viewModel.setCroppingCoverByUri(uri, CoverSource.Camera)
                            openCoverCropEditFragment()
                        }

                        override fun onGetFromGallery(uri: Uri) {
                            viewModel.setCroppingCoverByUri(uri, CoverSource.Gallery)
                            openCoverCropEditFragment()
                        }
                    },
                    intentHandler = { intent, requestCode ->
                        (activity as? PlayBroadcastActivity)?.stopPreview()
                        startActivityForResult(intent, requestCode)
                    }
            )
        }
        return imagePickerHelper
    }

    private fun openCoverCropEditFragment() {
        val fragmentFactory = childFragmentManager.fragmentFactory
        val fragmentInstance = fragmentFactory.instantiate(requireContext().classLoader, CoverCropEditBottomSheet::class.java.name) as CoverCropEditBottomSheet
        fragmentInstance.show(childFragmentManager)
    }
}