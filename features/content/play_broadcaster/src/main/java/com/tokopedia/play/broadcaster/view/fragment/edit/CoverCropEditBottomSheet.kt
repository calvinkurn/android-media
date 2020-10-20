package com.tokopedia.play.broadcaster.view.fragment.edit

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.view.contract.PlayBottomSheetCoordinator
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.fragment.PlayCoverSetupFragment
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import javax.inject.Inject

/**
 * Created by jegul on 22/06/20
 */
class CoverCropEditBottomSheet @Inject constructor(
        private val dialogCustomizer: PlayBroadcastDialogCustomizer
) : BottomSheetDialogFragment(), PlayBottomSheetCoordinator {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var flFragment: FrameLayout
    private lateinit var flOverlay: FrameLayout

    private var mListener: EditCoverResultListener? = null

    private val currentFragment: Fragment?
        get() = childFragmentManager.findFragmentById(flFragment.id)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
            override fun onBackPressed() {
                goBack()
            }
        }.apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_play_broadcast_setup, container, false)
        dialog?.let { setupDialog(it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun goBack() {
        val currFragment = currentFragment
        if (currFragment is PlayBaseSetupFragment) {
            val isIntercepted = currFragment.onInterceptBackPressed()
            if (!isIntercepted) cancelEdit()
        } else cancelEdit()
    }

    override fun <T : Fragment> navigateToFragment(fragmentClass: Class<out T>, extras: Bundle, sharedElements: List<View>, onFragment: (T) -> Unit) {
        //Not used
    }

    fun setListener(listener: EditCoverResultListener) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun initView(view: View) {
        with(view) {
            flFragment = findViewById(R.id.fl_fragment)
            flOverlay = findViewById(R.id.fl_overlay)
        }
    }

    private fun setupView(view: View) {
        flOverlay.setOnClickListener { dialog?.onBackPressed() }

        openCropFragment()
    }

    private fun openCropFragment() {
        val fragmentFactory = childFragmentManager.fragmentFactory
        val fragmentInstance = fragmentFactory.instantiate(requireContext().classLoader, PlayCoverSetupFragment::class.java.name) as PlayCoverSetupFragment
        fragmentInstance.setListener(object : PlayCoverSetupFragment.Listener {
            override suspend fun onCoverSetupFinished(dataStore: PlayBroadcastSetupDataStore): Throwable? {
                val error = mListener?.onSetupCompletedWithData(this@CoverCropEditBottomSheet, dataStore)
                return if (error == null) {
                    dismiss()
                    null
                }
                else error
            }

            override fun onCancelCropping(coverSource: CoverSource): Boolean {
                mListener?.onChangeCoverFromCropping(coverSource)
                dismiss()

                return true
            }
        })
        fragmentInstance.arguments = Bundle().apply {
            putBoolean(PlayCoverSetupFragment.EXTRA_IS_EDIT_COVER_MODE, true)
        }

        childFragmentManager.beginTransaction()
                .replace(flFragment.id, fragmentInstance)
                .commit()
    }

    private fun setupDialog(dialog: Dialog) {
        dialog.setOnShowListener {
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams = bottomSheet?.layoutParams?.apply {
                height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
            bottomSheet?.let {
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.peekHeight = maxHeight()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            isCancelable = false
        }
    }

    private fun maxHeight(): Int = getScreenHeight()

    private fun cancelEdit() {
        mListener?.onSetupCanceled()
        dismiss()
    }

    companion object {
        private const val TAG = "Cover Crop Edit BottomSheet"
    }

    interface EditCoverResultListener : SetupResultListener {

        fun onChangeCoverFromCropping(coverSource: CoverSource)
    }
}