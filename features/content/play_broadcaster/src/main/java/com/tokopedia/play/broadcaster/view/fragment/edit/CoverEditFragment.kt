package com.tokopedia.play.broadcaster.view.fragment.edit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.provider.PlayBroadcastComponentProvider
import com.tokopedia.play.broadcaster.di.setup.DaggerPlayBroadcastSetupComponent
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.util.helper.CoverImagePickerHelper
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

    private lateinit var imagePickerHelper: CoverImagePickerHelper

    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel
    private lateinit var viewModel: PlayCoverSetupViewModel

    private var mListener: SetupResultListener? = null

    override fun getScreenName(): String = "Cover Edit Fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        childFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayCoverSetupViewModel::class.java)
        dataStoreViewModel = ViewModelProviders.of(this, viewModelFactory).get(DataStoreViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_empty_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!getImagePickerHelper().onActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data)
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
        dataStoreViewModel.setDataStore(parentViewModel.getCurrentSetupDataStore())

        getImagePickerHelper().show()
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
                        startActivityForResult(intent, requestCode)
                    }
            )
        }
        return imagePickerHelper
    }

    private fun openCoverCropEditFragment() {
        val fragmentFactory = childFragmentManager.fragmentFactory
        val fragmentInstance = fragmentFactory.instantiate(requireContext().classLoader, CoverCropEditBottomSheet::class.java.name) as CoverCropEditBottomSheet
        mListener?.let { fragmentInstance.setListener(it) }

        fragmentInstance.show(childFragmentManager)
    }
}