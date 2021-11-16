package com.tokopedia.homecredit.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.homecredit.di.component.HomeCreditComponent
import com.tokopedia.homecredit.domain.model.ImageDetail
import com.tokopedia.homecredit.viewModel.HomeCreditViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifyprinciples.R
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.io.File
import java.util.*
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
    var finalCameraResultFilePath: String? = null
    protected var cameraOverlayImage: ImageView? = null
    protected var headerText: TextView? = null
    var cameraActionsRL: RelativeLayout? = null

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    var homeCreditViewModel: HomeCreditViewModel? = null
    private var mCapturingPicture = false
    private var flashIndex = 0
    private var supportedFlashList: MutableList<Flash>? = null
    private var mCaptureNativeSize: Size? = null
    private var progressDialog: ProgressDialog? = null
    override fun initInjector() {
        getComponent(HomeCreditComponent::class.java).inject(this)
    }

    override fun getScreenName(): String? {
        return null
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

    @SuppressLint("ResourcePackage")
    private fun setUIFlashCamera(flashEnum: Int) {
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.Unify_Static_White)
        if (flashEnum == Flash.AUTO.ordinal) {
            flashControl?.setImageDrawable(
                MethodChecker.getDrawable(
                    activity, com.tokopedia.imagepicker.common.R.drawable.ic_auto_flash
                )
            )
        } else if (flashEnum == Flash.ON.ordinal) {
            flashControl?.setImage(
                IconUnify.FLASH_ON,
                colorWhite,
                colorWhite,
                colorWhite,
                colorWhite
            )
        } else if (flashEnum == Flash.OFF.ordinal) {
            flashControl?.setImage(
                IconUnify.FLASH_OFF,
                colorWhite,
                colorWhite,
                colorWhite,
                colorWhite
            )
        }
    }

    fun initCameraProp() {
        cameraView?.open()
        cameraLayout?.visibility = View.VISIBLE
        imageCaptured?.visibility = View.GONE
        cameraActionsRL?.visibility = View.VISIBLE
        pictureActionLL?.visibility = View.GONE
    }

    fun initListeners() {
        homeCreditViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(
            HomeCreditViewModel::class.java
        )
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
        captureImage?.setOnClickListener { v: View? -> capturePicture() }
        flashControl?.setOnClickListener { v: View? ->
            if (supportedFlashList != null && supportedFlashList?.size ?: 0 > 0) {
                flashIndex = (flashIndex + 1) % supportedFlashList?.size!!
                setCameraFlash()
            }
        }
        reverseCamera?.setOnClickListener { v: View? -> toggleCamera() }
        retakePhoto?.setOnClickListener { v: View? -> initCameraProp() }
        buttonCancel?.setOnClickListener { v: View? -> requireActivity().finish() }
        cameraView?.addCameraListener(cameraListener as CameraListener)
        progressDialog = ProgressDialog(activity)
        progressDialog?.setCancelable(false)
        progressDialog?.setMessage(getString(com.tokopedia.homecredit.R.string.title_loading))
    }

    private fun generateImage(imageByte: ByteArray) {
        if (mCaptureNativeSize == null) {
            mCaptureNativeSize = cameraView?.pictureSize
        }
        mCaptureNativeSize?.let { mCaptureNativeSize ->
            homeCreditViewModel?.computeImageArray(
                imageByte,
                mCaptureNativeSize, fileLocationFromDirectory
            )
        }
        homeCreditViewModel?.imageDetailLiveData?.observe(
            this,
            { imageDetail: Result<ImageDetail>? ->
                captureImage?.isClickable = true
                if (imageDetail is Success<*> && (imageDetail as Success<ImageDetail>).data.imagePath != null) loadImageFromBitmap(
                    imageCaptured, imageDetail.data
                )
                hideCameraProp()
            })
        reset()
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
        hideLoading()
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
        if (isAdded) {
            progressDialog?.show()
        }
    }

    protected fun hideLoading() {
        if (isAdded) {
            progressDialog?.dismiss()
        }
    }

    private fun toggleCamera() {
        if (mCapturingPicture) {
            return
        }
        cameraView?.toggleFacing()
    }

    fun hideCameraProp() {
        cameraView?.close()
        cameraLayout?.visibility = View.GONE
        imageCaptured?.visibility = View.VISIBLE
        cameraActionsRL?.visibility = View.GONE
        pictureActionLL?.visibility = View.VISIBLE
    }

    @SuppressLint("ObsoleteSdkInt")
    fun onVisible() {
        if (requireActivity().isFinishing) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            val permission = Manifest.permission.CAMERA
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startCamera()
            }
        } else {
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

    @TargetApi(23)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onAttachActivity(context)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachActivity(activity)
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

    private val fileLocationFromDirectory: File
        private get() {
            val directory = ContextWrapper(activity).getDir(FOLDER_NAME, Context.MODE_PRIVATE)
            if (!directory.exists()) directory.mkdir()
            val imageName = System.currentTimeMillis().toString() + FILE_EXTENSIONS
            return File(directory.absolutePath, imageName)
        }

    companion object {
        private const val FOLDER_NAME = "extras"
        private const val FILE_EXTENSIONS = ".jpg"
    }
}