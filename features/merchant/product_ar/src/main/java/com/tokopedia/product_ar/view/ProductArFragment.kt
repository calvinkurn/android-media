package com.tokopedia.product_ar.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.modiface.mfemakeupkit.MFEMakeupEngine
import com.modiface.mfemakeupkit.effects.MFEMakeupProduct
import com.modiface.mfemakeupkit.widgets.MFEMakeupView
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.viewmodel.ProductArViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductArFragment : Fragment(), ProductArListener {

    companion object {
        @JvmStatic
        fun newInstance() = ProductArFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private var viewModel: ProductArViewModel? = null

    private var mMakeupView: MFEMakeupView? = null

    private var partialBottomArView: PartialBottomArView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_ar, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as? ProductArActivity)?.component?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background))
        }

        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductArViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMakeupView = view.findViewById(R.id.main_img)
        getMakeUpEngine()?.attachMakeupView(mMakeupView)
        partialBottomArView = PartialBottomArView.build(view, this)
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 123)
        } else {
            getMakeUpEngine()?.startRunningWithCamera(context)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    private fun observeData() {
        viewModel?.selectedProductArData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    partialBottomArView?.renderBottomInfo(it.data)
                }
                is Fail -> {
                }
            }
        }

        viewModel?.productArList?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    partialBottomArView?.renderRecyclerView(it.data)
                }
                is Fail -> {
                }
            }
        }

        viewModel?.mfeMakeUpLook?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    getMakeUpEngine()?.clearMakeupLook()
                    getMakeUpEngine()?.setMakeupLook(it.data)
                }
                is Fail -> {
                    // still noop
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMakeUpEngine()?.startRunningWithCamera(context)
            } else {
                AlertDialog.Builder(requireContext())
                        .setTitle("Permission Error")
                        .setMessage("The camera permission is needed for live mode. You must click allow to start the live try-on")
                        .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getMakeUpEngine()?.onResume(context)
    }

    override fun onPause() {
        getMakeUpEngine()?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        getMakeUpEngine()?.close()
    }

    override fun onVariantClicked(productId: String,
                                  isSelected: Boolean,
                                  selectedMfeProduct: MFEMakeupProduct) {
        if (isSelected) return
        viewModel?.let {
            it.onVariantClicked(
                    productId, it.getProductArUiModel(),
                    partialBottomArView?.adapter?.getCurrentArImageDatas() ?: listOf(),
                    selectedMfeProduct
            )
        }
    }

    private fun getMakeUpEngine(): MFEMakeupEngine? = (activity as? ProductArActivity)?.getMakeUpEngine()

}