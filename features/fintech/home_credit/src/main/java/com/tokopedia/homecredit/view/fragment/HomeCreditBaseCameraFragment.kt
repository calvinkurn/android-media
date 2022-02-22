package com.tokopedia.homecredit.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.homecredit.R
import com.tokopedia.homecredit.di.component.HomeCreditComponent
import com.tokopedia.homecredit.domain.model.ImageDetail
import com.tokopedia.homecredit.viewModel.HomeCreditViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

open class HomeCreditBaseCameraFragment : BaseDaggerFragment() {

    var cameraView: CameraView? = null
    var reverseCamera: View? = null
    var cameraListener: CameraListener? = null
    var isCameraOpen = false
    var retakePhoto: TextView? = null
    var continueUpload: TextView? = null
    var captureImage: View? = null
    var cameraLayout: FrameLayout? = null
    var flashControl: IconUnify? = null
    var buttonCancel: IconUnify? = null
    var imageCaptured: ImageView? = null
    var pictureActionLL: LinearLayout? = null
    var cameraActionsRL: RelativeLayout? = null
    var loaderDialog : LoaderDialog?= null

    protected var cameraOverlayImage: ImageView? = null
    protected var headerText: TextView? = null

    var finalCameraResultFilePath: String? = null

    private var mCapturingPicture = false
    private var flashIndex = 0
    private var supportedFlashList: MutableList<Flash>? = null
    private var mCaptureNativeSize: Size? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val homeCreditViewModel: HomeCreditViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(HomeCreditViewModel::class.java)
    }

    override fun initInjector() {
        getComponent(HomeCreditComponent::class.java).inject(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    fun initialFlash() {
        supportedFlashList = ArrayList()
        if (cameraView == null || cameraView?.cameraOptions == null) {
            return
        }
        val flashSet = cameraView?.cameraOptions?.supportedFlash
        if (flashSet != null) {
            for (flash in flashSet) {
                if (flash != Flash.TORCH) {
                    (supportedFlashList as ArrayList<Flash>).add(flash)
                }
            }
        }
        if (supportedFlashList != null && (supportedFlashList as ArrayList<Flash>).size > 0) {
            flashControl?.visibility = View.VISIBLE
            setCameraFlash()
        } else {
            flashControl?.visibility = View.GONE
        }
    }

    private fun setCameraFlash() {
        if (supportedFlashList == null || flashIndex < 0 || supportedFlashList?.size ?: 0 <= flashIndex) {
            return
        }
        var flash = supportedFlashList!![flashIndex]
        if (flash.ordinal == Flash.TORCH.ordinal) {
            flashIndex = (flashIndex + 1) % supportedFlashList!!.size
            flash = supportedFlashList!![flashIndex]
        }
        cameraView?.set(flash)
        setUIFlashCamera(flash.ordinal)
    }

    private fun setUIFlashCamera(flashEnum: Int) {
        context?.let {
            val colorWhite = ContextCompat.getColor(
                requireContext(),
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            )
            when (flashEnum) {
                Flash.AUTO.ordinal -> {
                    flashControl?.setImageDrawable(
                        MethodChecker.getDrawable(
                            activity,
                            R.drawable.home_credit_ic_auto_flash
                        )
                    )
                }
                Flash.ON.ordinal -> {
                    flashControl?.setImage(
                        IconUnify.FLASH_ON, colorWhite,
                        colorWhite, colorWhite, colorWhite
                    )
                }
                Flash.OFF.ordinal -> {
                    flashControl?.setImage(
                        IconUnify.FLASH_OFF, colorWhite,
                        colorWhite, colorWhite, colorWhite
                    )
                }
                else -> {}
            }
        }
    }

    private fun initCameraProp() {
        cameraView?.open()
        cameraLayout?.visibility = View.VISIBLE
        imageCaptured?.visibility = View.GONE
        cameraActionsRL?.visibility = View.VISIBLE
        pictureActionLL?.visibility = View.GONE
    }

    fun initListeners() {
        initCameraListener()
        captureImage?.setOnClickListener { capturePicture() }
        flashControl?.setOnClickListener {
            if (supportedFlashList != null && supportedFlashList?.size ?: 0 > 0) {
                flashIndex = (flashIndex + 1) % supportedFlashList?.size!!
                setCameraFlash()
            }
        }
        reverseCamera?.setOnClickListener { toggleCamera() }
        retakePhoto?.setOnClickListener { initCameraProp() }
        buttonCancel?.setOnClickListener { requireActivity().finish() }
        cameraView?.addCameraListener(cameraListener as CameraListener)
    }

    private fun initCameraListener(){
        cameraListener = object : CameraListener() {
            override fun onCameraOpened(options: CameraOptions) {
                initialFlash()
                isCameraOpen = true
            }

            override fun onCameraClosed() {
                super.onCameraClosed()
                isCameraOpen = false
            }

            override fun onPictureTaken(result: PictureResult) {
                try {
                    generateImage(result.data)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun generateImage(imageByte: ByteArray) {
        if (mCaptureNativeSize == null) {
            mCaptureNativeSize = cameraView?.pictureSize
        }
        homeCreditViewModel?.computeImageArray(imageByte, mCaptureNativeSize)
    }

    private fun observeViewModel() {
        homeCreditViewModel?.imageDetailLiveData?.observe(viewLifecycleOwner,
            { imageDetail: Result<ImageDetail>? ->
                captureImage?.isClickable = true
                when (imageDetail) {
                    is Success -> {
                        finalCameraResultFilePath = imageDetail.data.imagePath ?: ""
                        loadImageFromBitmap(imageCaptured, imageDetail.data)
                    }

                    else ->{}
                }
                reset()
                hideLoading()
                hideCameraProp()
            })
    }

    private fun loadImageFromBitmap(imageView: ImageView?, data: ImageDetail) {
        val width = data.bitMapWidth
        val height = data.bitmapHeight
        val min: Int
        val max: Int
        if (width > height) {
            min = height
            max = width
        } else {
            min = width
            max = height
        }
        val loadFitCenter = min != 0 && max / min > 2
        if (loadFitCenter)
            imageView?.let {
                Glide.with(requireContext()).load(data.imagePath).fitCenter().into(it)
            }
        else
            imageView?.let { Glide.with(requireContext()).load(data.imagePath).into(it) }
    }

    private fun reset() {
        mCapturingPicture = false
        mCaptureNativeSize = null
    }

    private fun capturePicture() {
        if (mCapturingPicture || !isCameraOpen) {
            return
        }
        showLoading()
        mCapturingPicture = true
        mCaptureNativeSize = cameraView?.pictureSize
        cameraView?.takePicture()
        captureImage?.isClickable = false
    }

    private fun showLoading() {
        if (isAdded && context!= null) {
            loaderDialog = LoaderDialog(requireContext()).apply {
                setLoadingText(getString(R.string.title_loading))
            }
            loaderDialog?.show()
        }
    }

    private fun hideLoading() {
        if (isAdded) {
            loaderDialog?.dialog?.dismiss()
            loaderDialog = null
        }
    }

    private fun toggleCamera() {
        if (mCapturingPicture) {
            return
        }
        cameraView?.toggleFacing()
    }

    private fun hideCameraProp() {
        cameraView?.close()
        cameraLayout?.visibility = View.GONE
        imageCaptured?.visibility = View.VISIBLE
        cameraActionsRL?.visibility = View.GONE
        pictureActionLL?.visibility = View.VISIBLE
    }

    private fun onVisible() {
        if (requireActivity().isFinishing)
            return
        val permission = Manifest.permission.CAMERA
        if (ActivityCompat.checkSelfPermission(requireContext(), permission)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        }
    }

    private fun startCamera() {
        try {
            cameraView?.clearCameraListeners()
            cameraListener?.let { cameraView?.addCameraListener(it) }
            cameraView?.open()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    override fun onPause() {
        super.onPause()
        if (isCameraOpen) {
            cameraView?.close()
        }
    }

    override fun onDestroy() {
        hideLoading()
        cameraView?.close()
        super.onDestroy()
    }

}