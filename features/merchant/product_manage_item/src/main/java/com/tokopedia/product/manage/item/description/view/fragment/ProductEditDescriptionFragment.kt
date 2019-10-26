package com.tokopedia.product.manage.item.description.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.description.view.activity.ProductAddDescriptionPickerActivity
import com.tokopedia.product.manage.item.description.view.activity.ProductAddDescriptionPickerActivity.PRODUCT_DESCRIPTION
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_DESCRIPTION
import com.tokopedia.product.manage.item.description.view.model.ProductDescription
import com.tokopedia.product.manage.item.video.view.activity.ProductAddVideoActivity
import com.tokopedia.product.manage.item.video.view.fragment.ProductAddVideoFragment.Companion.EXTRA_KEYWORD
import com.tokopedia.product.manage.item.video.view.fragment.ProductAddVideoFragment.Companion.EXTRA_VIDEOS_LINKS
import kotlinx.android.synthetic.main.fragment_product_edit_description.*
import java.util.ArrayList

class ProductEditDescriptionFragment : Fragment() {

    private var videoIDsTemp: ArrayList<String> = ArrayList()
    private var productDescription = ProductDescription()
    private lateinit var keyword: String
    private val texViewMenu: TextView? by lazy { activity?.findViewById(R.id.texViewMenu) as? TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.run {
            if(intent.hasExtra(EXTRA_DESCRIPTION)) {
                productDescription = intent.getParcelableExtra(EXTRA_DESCRIPTION)
            }
            keyword = intent.getStringExtra(EXTRA_KEYWORD)
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_PRODUCT_DESCRIPTION)) {
                productDescription = savedInstanceState.getParcelable(SAVED_PRODUCT_DESCRIPTION)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataDescription(productDescription)
        texViewMenu?.run {
            text = getString(R.string.label_save)
            setOnClickListener {
                setResult()
            }}
        labelViewVideoProduct.setOnClickListener {
            startActivityForResult(Intent(context, ProductAddVideoActivity::class.java)
                .putExtra(EXTRA_VIDEOS_LINKS, videoIDsTemp)
                    .putExtra(EXTRA_KEYWORD, keyword), REQUEST_CODE_GET_VIDEO) }

        editTextDescription.setOnClickListener { goToProductDescriptionPicker(editTextDescription.text.toString()) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_GET_DESCRIPTION -> {
                    val description = data.getStringExtra(PRODUCT_DESCRIPTION)
                    editTextDescription.setText(description)
                }
                REQUEST_CODE_GET_VIDEO -> {
                    videoIDsTemp = data.getStringArrayListExtra(EXTRA_VIDEOS_LINKS)
                    setLabelViewVideo(videoIDsTemp)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setDataDescription(productDescription: ProductDescription){
        editTextDescription.setText(productDescription.description)
        editTextFeature.setText(productDescription.feature)
        labelSwitchNewCondition.isChecked = productDescription.isNew
        videoIDsTemp = productDescription.videoIDs
        setLabelViewVideo(productDescription.videoIDs)
    }

    private fun setLabelViewVideo(videoList: ArrayList<String>){
        labelViewVideoProduct.setContent(if (videoList.size == 0) getString(R.string.label_add) else getString(R.string.product_count_video, videoList.size))
    }

    private fun saveData(productDescription: ProductDescription) = productDescription.apply {
            description = editTextDescription.text.toString()
            feature = editTextFeature.text.toString()
            isNew = labelSwitchNewCondition.isChecked
            videoIDs = videoIDsTemp
        }


    private fun setResult(){
        activity?.let {
            it.setResult(Activity.RESULT_OK, Intent().apply { putExtra(EXTRA_DESCRIPTION, saveData(productDescription)) })
            it.finish()
        }
    }

    private fun goToProductDescriptionPicker(description: String) {
        startActivityForResult(ProductAddDescriptionPickerActivity.start(activity, description), REQUEST_CODE_GET_DESCRIPTION)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_PRODUCT_DESCRIPTION, saveData(productDescription))
    }

    companion object {
        const val SAVED_PRODUCT_DESCRIPTION = "SAVED_PRODUCT_DESCRIPTION"
        const val REQUEST_CODE_GET_DESCRIPTION = 0
        const val REQUEST_CODE_GET_VIDEO = 1
        fun createInstance() = ProductEditDescriptionFragment()
    }
}
