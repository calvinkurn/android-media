package com.tokopedia.mvcwidget.views

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.mvcwidget.*
import com.tokopedia.mvcwidget.di.components.DaggerMvcComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class MvcDetailView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var rv: RecyclerView
    var tvTitle: TextView
    var viewFlipper: ViewFlipper
    var globalError: GlobalError

    var mvcFollowContainer: MvcFollowViewContainer
    private val listItem = arrayListOf<MvcListItem>()
    private val adapter = CouponAdapter(listItem)

    private val CONTAINER_CONTENT = 0
    private val CONTAINER_SHIMMER = 1
    private val CONTAINER_ERROR = 2
    var shopId = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MvcDetailViewModel


    init {
        View.inflate(context, R.layout.mvc_detail_view, this)
        rv = findViewById(R.id.rv)
        viewFlipper = findViewById(R.id.viewFlipper)
        mvcFollowContainer = findViewById(R.id.mvc_follow_view_container)
        tvTitle = findViewById(R.id.tvTitleMvcDetailView)
        globalError = findViewById(R.id.mvcDetailGlobalError)

        DaggerMvcComponent.builder()
                .build().inject(this)

        if (context is AppCompatActivity) {
            val viewModelProvider = ViewModelProviders.of(context, viewModelFactory)
            viewModel = viewModelProvider[MvcDetailViewModel::class.java]
        }

        setupListeners()
    }

    fun setupListeners() {
        viewModel.listLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    toggleLoading(true)
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    handleSuccess(it.data)
                }
                LiveDataResult.STATUS.ERROR -> {
                    handleError(it.error)
                }
            }
        })
        viewModel.membershipLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    toggleLoading(true)
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    handleMembershipRegistrationSuccess(it.data)
                }
                LiveDataResult.STATUS.ERROR -> {
                    handleMembershipRegistrationError(it.error)
                }
            }
        })

        viewModel.followLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    toggleLoading(true)
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    handleFollowSuccess(it.data)
                }
                LiveDataResult.STATUS.ERROR -> {
                    handleFollowFail(it.error)
                }
            }
        })


        globalError.setActionClickListener {
            viewModel.getListData(shopId)
        }

        mvcFollowContainer.oneActionView.btn.setOnClickListener {
            viewModel.followShop()
        }

        mvcFollowContainer.twoActionView.btnSecond.setOnClickListener {
            viewModel.registerMembership()
        }
    }

    private fun toggleLoading(showLoading: Boolean) {
        if (showLoading) {
            viewFlipper.displayedChild = CONTAINER_SHIMMER
        } else {
            viewFlipper.displayedChild = CONTAINER_CONTENT
        }
    }

    private fun handleError(th: Throwable?) {
        viewFlipper.displayedChild = CONTAINER_ERROR
    }

    private fun handleSuccess(tokopointsCatalogMVCListResponse: TokopointsCatalogMVCListResponse?) {
        if (tokopointsCatalogMVCListResponse == null) handleError(null)
        tokopointsCatalogMVCListResponse?.let { response ->
            toggleLoading(false)
            setupData(response)
        }
    }

    private fun handleMembershipRegistrationSuccess(message: String?) {
        if (!message.isNullOrEmpty())
            Toaster.build(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleMembershipRegistrationError(th: Throwable?) {
        toggleLoading(false)
        if (!th?.message.isNullOrEmpty()) {
            Toaster.build(this, th!!.message!!, Toast.LENGTH_SHORT, Toaster.TYPE_ERROR, context.getString(R.string.mvc_coba_lagi), OnClickListener {
                mvcFollowContainer.twoActionView.performClick()
            })
        }
    }

    private fun handleFollowSuccess(message: String?) {
        if (!message.isNullOrEmpty())
            Toaster.build(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleFollowFail(th: Throwable?) {
        toggleLoading(false)
        if (!th?.message.isNullOrEmpty()) {
            Toaster.build(this, th!!.message!!, Toast.LENGTH_SHORT, Toaster.TYPE_ERROR, context.getString(R.string.mvc_coba_lagi), OnClickListener {
                mvcFollowContainer.oneActionView.performClick()
            })
        }
    }

    fun show(shopId: String) {
        this.shopId = shopId
        viewModel.getListData(shopId)
    }

    fun setupData(response: TokopointsCatalogMVCListResponse) {

        response.data?.followWidget?.let {
            setupFollowView(it)
        }

        val hardcodedText = "Kupon bisa dipilih lewat keranjang jika ada barang dari toko"
        val shopName = response.data?.shopName ?: ""
        val finalText = "$hardcodedText $shopName"
        val spannableString = SpannableString(finalText)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), finalText.length - shopName.length - 1, finalText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvTitle.text = spannableString

        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter
//        val dividerDrawable = ContextCompat.getDrawable(context, R.drawable.mvc_line_separator)
//        dividerDrawable?.let {
//            val itemDecoration = object :RecyclerView.ItemDecoration(){
//                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
//                    super.onDraw(c, parent, state)
//                    dividerDrawable.draw(c)
//                }
//            }
//            rv.addItemDecoration(itemDecoration)
//        }

        response.data?.catalogList?.forEach {
            var quotaTextLength = 0
            if (it != null) {

                val sb = StringBuilder()
                if (!it.expiredLabel.isNullOrEmpty()) {
                    sb.append(it.expiredLabel)
                }
                if (!it.quotaLeftLabel.isNullOrEmpty()) {
                    if (sb.isNotEmpty()) {
                        sb.append(" • ")
                    }
                    quotaTextLength = it.quotaLeftLabel.length
                    sb.append(it.quotaLeftLabel)
                }
                val spannableString2 = SpannableString(sb.toString())

                spannableString2.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.mvcw_red)), sb.toString().length - quotaTextLength, sb.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                val mvcListItem = MvcListItem(it.tagImageURLs, it.title ?: "", it.minimumUsageLabel
                        ?: "", spannableString2)
                listItem.add(mvcListItem)
                listItem.add(mvcListItem)
            }
        }

        //val mvcListItem = MvcListItem("", "Cashback 10% hingga Rp150.000", "Transaksi min. Rp700.000", SpannableString("Berakhir dalam 10 hari • Sisa 20 kupon"))
        adapter.notifyDataSetChanged()

    }

    private fun setupFollowView(followWidget: FollowWidget) {
        mvcFollowContainer.setData(followWidget)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewModel.listLiveData.removeObservers(context as AppCompatActivity)
        viewModel.membershipLiveData.removeObservers(context as AppCompatActivity)
    }
}

class MvcListItem(val urlList: List<String?>?, val title1: String, val title2: String, val title3: SpannableString)

class CouponAdapter(val data: List<MvcListItem>) : RecyclerView.Adapter<CouponListItemVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponListItemVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.mvc_list_item_coupon, parent, false)
        return CouponListItemVH(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CouponListItemVH, position: Int) {
        holder.setData(data[position])
        holder.divider.visibility = if (position == data.size - 1) View.INVISIBLE else View.VISIBLE
    }
}

class CouponListItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val rvImage: RecyclerView = itemView.findViewById(R.id.rvImage)
    val tv1: Typography = itemView.findViewById(R.id.tv1)
    val tv2: Typography = itemView.findViewById(R.id.tv2)
    val tv3: Typography = itemView.findViewById(R.id.tv3)
    val divider: View = itemView.findViewById(R.id.divider)

    init {
        rvImage.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

    }

    fun setData(data: MvcListItem) {
        tv1.text = data.title1
        tv2.text = data.title2
        tv3.text = data.title3

        if (!data.urlList.isNullOrEmpty()) {
            rvImage.adapter = ImageAdapter(data.urlList)
            (rvImage.adapter as ImageAdapter).notifyDataSetChanged()
        }
    }

    class ImageAdapter(val urlList: List<String?>) : RecyclerView.Adapter<ListItemImageVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemImageVH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.mvc_list_item_images, parent, false)
            return ListItemImageVH(v)
        }

        override fun getItemCount() = urlList.size

        override fun onBindViewHolder(holder: ListItemImageVH, position: Int) {
            if (!urlList[position].isNullOrEmpty()) {
                Glide.with(holder.image)
                        .load(urlList[position])
                        .into(holder.image)
            }
        }

    }

    class ListItemImageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: AppCompatImageView = itemView.findViewById(R.id.image)
    }
}