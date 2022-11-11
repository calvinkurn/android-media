package com.tokopedia.privacycenter.main.section.privacypolicy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.databinding.SectionPrivacyPolicyBinding
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.DEFAULT_TITLE
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.KEY_HTML_CONTENT
import com.tokopedia.privacycenter.main.section.privacypolicy.adapter.PrivacyPolicyAdapter
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDataModel
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDetailDataModel
import com.tokopedia.privacycenter.main.section.privacypolicy.webview.PrivacyPolicyWebViewActivity
import com.tokopedia.unifycomponents.Toaster

class PrivacyPolicySection constructor(
    private val context: Context?,
    private val fragmentManager: FragmentManager?,
    private val viewModel: PrivacyPolicySectionViewModel
) : BasePrivacyCenterSection(context), PrivacyPolicyAdapter.Listener {

    private var isPrivacyPolicyListOpened: Boolean = false
    private val privacyPolicyAdapter = PrivacyPolicyAdapter(this)
    private var privacyPolicySectionBottomSheet: PrivacyPolicySectionBottomSheet? = null

    override val sectionViewBinding: SectionPrivacyPolicyBinding = SectionPrivacyPolicyBinding.inflate(
        LayoutInflater.from(context)
    )
    override val sectionTextTitle: String? = context?.resources?.getString(R.string.privacy_policy_section_title)
    override val sectionTextDescription: String? = context?.resources?.getString(R.string.privacy_policy_section_description)
    override val isShowDirectionButton: Boolean = false
    override val isFromBottomSheet: Boolean = false

    override fun onButtonDirectionClick(view: View) {
        // no op
    }

    override fun onViewRendered() {
        context?.let { NetworkClient.init(it) }
        showShimmering(false)

        sectionViewBinding.apply {
            listPrivacyPolicy.apply {
                adapter = privacyPolicyAdapter
            }

            menuCurrentPrivacyPolicy.setOnClickListener {
                val intent = RouteManager.getIntent(
                    context,
                    ApplinkConstInternalGlobal.WEBVIEW_TITLE,
                    DEFAULT_TITLE,
                    PrivacyPolicyConst.CURRENT_PRIVACY_URL
                )

                context?.startActivity(intent)
            }

            menuListPrivacyPolicy.setOnClickListener {
                isPrivacyPolicyListOpened = !isPrivacyPolicyListOpened
                iconMenu.setImage(
                    if (isPrivacyPolicyListOpened)
                        IconUnify.CHEVRON_UP
                    else
                        IconUnify.CHEVRON_DOWN
                )
                listPrivacyPolicy.showWithCondition(isPrivacyPolicyListOpened)
            }

            buttonShowAllList.setOnClickListener {
                if (privacyPolicySectionBottomSheet == null) {
                    privacyPolicySectionBottomSheet = PrivacyPolicySectionBottomSheet()
                }

                fragmentManager?.let {
                    privacyPolicySectionBottomSheet?.show(it, PrivacyPolicySectionBottomSheet.TAG)
                }
            }
        }

        viewModel.getPrivacyPolicyTopFiveList()

        // TODO: Remove if already tested with real API
        onSuccessGetPrivacyPolicyTopFiveList(
            listOf(
                PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
                PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
                PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
                PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
                PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
            )
        )
    }

    override fun initObservers() {
        lifecycleOwner?.run {
            viewModel.privacyPolicyTopFiveList.observe(this) {
                when (it) {
                    is PrivacyCenterStateResult.Fail -> showLocalLoad()
                    is PrivacyCenterStateResult.Loading -> loadingPrivacyPolicyList(true)
                    is PrivacyCenterStateResult.Success -> onSuccessGetPrivacyPolicyTopFiveList(it.data)
                }
            }

            viewModel.privacyPolicyDetail.observe(this) {
                when (it) {
                    is PrivacyCenterStateResult.Fail -> {
                        Toaster.build(sectionViewBinding.root, it.error.message.toString()).show()
                    }
                    is PrivacyCenterStateResult.Loading -> { }
                    is PrivacyCenterStateResult.Success -> onSuccessGetPrivacyPolicyDetail(it.data)
                }
            }
        }
    }

    override fun onItemClicked(item: PrivacyPolicyDataModel) {
        viewModel.getPrivacyPolicyDetail(item.sectionId)

        // TODO: Remove if already tested with real API
        openDetailPrivacyPolicy(item.sectionId, RAW_HTML_TEST)
    }

    private fun onSuccessGetPrivacyPolicyTopFiveList(data: List<PrivacyPolicyDataModel>) {
        loadingPrivacyPolicyList(false)

        privacyPolicyAdapter.apply {
            clearAllItems()
            addItems(data)
            notifyItemRangeInserted(0, data.size)
        }
    }

    private fun onSuccessGetPrivacyPolicyDetail(data: PrivacyPolicyDetailDataModel) {
        openDetailPrivacyPolicy(data.sectionTitle, data.sectionContent)
    }

    private fun openDetailPrivacyPolicy(title: String, htmlContent: String) {
        val intent = Intent(context, PrivacyPolicyWebViewActivity::class.java).apply {
            putExtras(Bundle().apply {
                putString(PrivacyPolicyWebViewActivity.KEY_TITLE, title)
                putString(KEY_HTML_CONTENT, htmlContent)
            })
        }

        context?.startActivity(intent)
    }

    private fun loadingPrivacyPolicyList(isLoading: Boolean) {
        sectionViewBinding.apply {
            loaderListPrivacyPolicy.showWithCondition(isLoading)
            listPrivacyPolicy.showWithCondition(!isLoading)
        }
    }

    private fun showLocalLoad() {
        sectionViewBinding.apply {
            loaderListPrivacyPolicy.hide()
            listPrivacyPolicy.hide()
            localLoadPrivacyPolicy.apply {
                localLoadTitle = context.getString(R.string.privacy_center_error_network_title)
                refreshBtn?.setOnClickListener {
                    progressState = true
                    viewModel.getPrivacyPolicyTopFiveList()
                }
            }
        }
    }

    companion object {
        const val TAG = "PrivacyPolicy"

        // TODO: Remove if already tested with real API
        const val RAW_HTML_TEST = "<!DOCTYPE html>\n" +
            "<html lang=\"id\" data-rh=\"lang\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title data-rh=\"data-rh\">Privacy Policy | Tokopedia</title><meta data-rh=\"true\" name=\"viewport\" content=\"initial-scale=1, minimum-scale=1, maximum-scale=5, user-scalable=no, width=device-width\"><meta data-rh=\"true\" name=\"page-type\" content=\"terms-privacy\"><link data-rh=\"true\" rel=\"icon\" href=\"https://www.tokopedia.com/favicon.ico\"><link data-chunk=\"main\" rel=\"preload\" as=\"script\" href=\"./Privacy Policy _ Tokopedia_files/runtime.d4bc8a62da703472b33e.js\" crossorigin=\"anonymous\" nonce=\"\"><link data-chunk=\"main\" rel=\"preload\" as=\"script\" href=\"./Privacy Policy _ Tokopedia_files/apollo.076d5a8ed52c5684b012.js\" crossorigin=\"anonymous\" nonce=\"\"><link data-chunk=\"main\" rel=\"preload\" as=\"script\" href=\"./Privacy Policy _ Tokopedia_files/unify.5b7b3134ed42f54ec842.js\" crossorigin=\"anonymous\" nonce=\"\"><link data-chunk=\"main\" rel=\"preload\" as=\"script\" href=\"./Privacy Policy _ Tokopedia_files/framework.e4540b4ca8956a8f8444.js\" crossorigin=\"anonymous\" nonce=\"\"><link data-chunk=\"main\" rel=\"preload\" as=\"script\" href=\"./Privacy Policy _ Tokopedia_files/vendor.d5794ffac83a33df3021.js\" crossorigin=\"anonymous\" nonce=\"\"><link data-chunk=\"main\" rel=\"preload\" as=\"script\" href=\"./Privacy Policy _ Tokopedia_files/main.4112d8daf3ab972ce2d9.js\" crossorigin=\"anonymous\" nonce=\"\"><link data-chunk=\"routes-terms-privacy\" rel=\"preload\" as=\"script\" href=\"./Privacy Policy _ Tokopedia_files/chunk.6620.0cc8117e1ac429bb2d19.js\" crossorigin=\"anonymous\" nonce=\"\"><link data-chunk=\"routes-terms-privacy\" rel=\"preload\" as=\"script\" href=\"./Privacy Policy _ Tokopedia_files/chunk.1175.b11962287510b87d9666.js\" crossorigin=\"anonymous\" nonce=\"\"><link data-chunk=\"routes-terms-privacy\" rel=\"preload\" as=\"script\" href=\"./Privacy Policy _ Tokopedia_files/chunk.routes-terms-privacy.3ba5f4a53c0051878eac.js\" crossorigin=\"anonymous\" nonce=\"\"><style type=\"text/css\" data-source=\"skipper-global\">@font-face{font-family:'Open Sauce One';font-weight:400;font-style:normal;font-display:swap;src:local('Open Sauce One Regular'),url('https://assets.tokopedia.net/asts/unify/fonts/OpenSauceOne-Regular.woff2') format('woff2');}@font-face{font-family:'Open Sauce One';font-weight:700;font-style:normal;font-display:swap;src:local('Open Sauce One ExtraBold'),url('https://assets.tokopedia.net/asts/unify/fonts/OpenSauceOne-ExtraBold.woff2') format(\"woff2\");}</style><style type=\"text/css\" data-source=\"skipper-global\">html{line-height:1.15;-webkit-text-size-adjust:100%}body{margin:0}main{display:block}h1{font-size:2em;margin:.67em 0}hr{box-sizing:content-box;height:0;overflow:visible}pre{font-family:monospace,monospace;font-size:1em}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;text-decoration:underline dotted}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace,monospace;font-size:1em}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-.25em}sup{top:-.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0}button,input{overflow:visible}button,select{text-transform:none}[type=button],[type=reset],[type=submit],button{-webkit-appearance:button}[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner,button::-moz-focus-inner{border-style:none;padding:0}[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring,button:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:.35em .75em .625em}legend{box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{box-sizing:border-box;padding:0}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit}details{display:block}summary{display:list-item}[hidden],template{display:none}</style><style type=\"text/css\" data-source=\"skipper-global\">*:focus{outline:0}html{box-sizing:border-box;font-size:14px;font-family:'Open Sauce One',-apple-system,BlinkMacSystemFont,'Segoe UI','Roboto','Oxygen','Ubuntu','Cantarell','Fira Sans','Droid Sans','Helvetica Neue',sans-serif}html,body{margin:0;padding:0;height:100%;color:rgba(0,0,0,.54)}*,*::before,*::after{box-sizing:inherit}::-webkit-input-placeholder{color:rgba(0,0,0,.26)}::-moz-placeholder{color:rgba(0,0,0,.26)}:-ms-input-placeholder{color:rgba(0,0,0,.26)}:-moz-placeholder{color:rgba(0,0,0,.26)}a{color:rgba(0,0,0,.7);text-decoration:none}button{font-family:'Open Sauce One',-apple-system,BlinkMacSystemFont,'Segoe UI','Roboto','Oxygen','Ubuntu','Cantarell','Fira Sans','Droid Sans','Helvetica Neue',sans-serif}input[type='text'],textarea,select,.input{font-family:'Open Sauce One',-apple-system,BlinkMacSystemFont,'Segoe UI','Roboto','Oxygen','Ubuntu','Cantarell','Fira Sans','Droid Sans','Helvetica Neue',sans-serif;-webkit-appearance:none}[type='search']{-webkit-appearance:none;outline-offset:0}input[type='number']::-webkit-inner-spin-button,input[type='number']::-webkit-outer-spin-button{-webkit-appearance:none;margin:.25em}</style><style type=\"text/css\" data-emotion-css=\"css jlk0mk 17fxu0k 3j3ykd ovhcia 1s16om9 1przuqo i6183d 1lz1btd 4o66f 10x2v5k 98wu2y xhy3fo 4dw8w1 1sj7go5 egjdad 6l2kgb 1rwki9m 13rn2wn kovptw 1ac3sm7 wz4948 1hb0hhl 1h9tmr9 i3wblx z048k7 bd18ux 1c4ya6b ngxyrx 1inzcwr 6mmbwl 1uoghlk 1buceln wr90yg 1lj5r9c 1j306kg-unf-loader-line z2zl1v-unf-loader-line 1ggf74l 1705l5b 192yzj n7n7wr 1thpqdv qscw6i 1tbxokc 30ado7\">@-webkit-keyframes animation-jlk0mk{0%{background-position:-300px;}100%{background-position:300px;}}@keyframes animation-jlk0mk{0%{background-position:-300px;}100%{background-position:300px;}}@-webkit-keyframes animation-17fxu0k{0%{-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%);}50%{-webkit-transform:translateY(50%);-ms-transform:translateY(50%);transform:translateY(50%);}100%{-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%);}}@keyframes animation-17fxu0k{0%{-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%);}50%{-webkit-transform:translateY(50%);-ms-transform:translateY(50%);transform:translateY(50%);}100%{-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%);}}@-webkit-keyframes animation-3j3ykd{0%{-webkit-transform:translateY(50%);-ms-transform:translateY(50%);transform:translateY(50%);}50%{-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%);}100%{-webkit-transform:translateY(50%);-ms-transform:translateY(50%);transform:translateY(50%);}}@keyframes animation-3j3ykd{0%{-webkit-transform:translateY(50%);-ms-transform:translateY(50%);transform:translateY(50%);}50%{-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%);}100%{-webkit-transform:translateY(50%);-ms-transform:translateY(50%);transform:translateY(50%);}}@-webkit-keyframes animation-ovhcia{100%{-webkit-transform:rotate(360deg);-ms-transform:rotate(360deg);transform:rotate(360deg);}}@keyframes animation-ovhcia{100%{-webkit-transform:rotate(360deg);-ms-transform:rotate(360deg);transform:rotate(360deg);}}@-webkit-keyframes animation-1s16om9{0%{stroke-dasharray:1,200;stroke-dashoffset:0;}50%{stroke-dasharray:89,200;stroke-dashoffset:-35;}100%{stroke-dasharray:89,200;stroke-dashoffset:-124;}}@keyframes animation-1s16om9{0%{stroke-dasharray:1,200;stroke-dashoffset:0;}50%{stroke-dasharray:89,200;stroke-dashoffset:-35;}100%{stroke-dasharray:89,200;stroke-dashoffset:-124;}}@-webkit-keyframes animation-1przuqo{0%{-webkit-transform:rotate(0deg);-ms-transform:rotate(0deg);transform:rotate(0deg);}100%{-webkit-transform:rotate(360deg);-ms-transform:rotate(360deg);transform:rotate(360deg);}}@keyframes animation-1przuqo{0%{-webkit-transform:rotate(0deg);-ms-transform:rotate(0deg);transform:rotate(0deg);}100%{-webkit-transform:rotate(360deg);-ms-transform:rotate(360deg);transform:rotate(360deg);}}@-webkit-keyframes animation-i6183d{0%{-webkit-transform:translateY(-60%);-ms-transform:translateY(-60%);transform:translateY(-60%);}50%{-webkit-transform:translateY(60%);-ms-transform:translateY(60%);transform:translateY(60%);}100%{-webkit-transform:translateY(-60%);-ms-transform:translateY(-60%);transform:translateY(-60%);}}@keyframes animation-i6183d{0%{-webkit-transform:translateY(-60%);-ms-transform:translateY(-60%);transform:translateY(-60%);}50%{-webkit-transform:translateY(60%);-ms-transform:translateY(60%);transform:translateY(60%);}100%{-webkit-transform:translateY(-60%);-ms-transform:translateY(-60%);transform:translateY(-60%);}}@-webkit-keyframes animation-1lz1btd{0%{-webkit-transform:translateY(60%);-ms-transform:translateY(60%);transform:translateY(60%);}50%{-webkit-transform:translateY(-60%);-ms-transform:translateY(-60%);transform:translateY(-60%);}100%{-webkit-transform:translateY(60%);-ms-transform:translateY(60%);transform:translateY(60%);}}@keyframes animation-1lz1btd{0%{-webkit-transform:translateY(60%);-ms-transform:translateY(60%);transform:translateY(60%);}50%{-webkit-transform:translateY(-60%);-ms-transform:translateY(-60%);transform:translateY(-60%);}100%{-webkit-transform:translateY(60%);-ms-transform:translateY(60%);transform:translateY(60%);}}@-webkit-keyframes animation-4o66f{0%{background-color:#FFC400;-webkit-transform:scaleX(0);-ms-transform:scaleX(0);transform:scaleX(0);}25%{-webkit-transform:scaleX(0);-ms-transform:scaleX(0);transform:scaleX(0);}50%{-webkit-transform:scaleX(1);-ms-transform:scaleX(1);transform:scaleX(1);}75%{-webkit-transform:scaleX(1);-ms-transform:scaleX(1);transform:scaleX(1);}100%{background-color:#FFC400;-webkit-transform:translateX(100%);-ms-transform:translateX(100%);transform:translateX(100%);}}@keyframes animation-4o66f{0%{background-color:#FFC400;-webkit-transform:scaleX(0);-ms-transform:scaleX(0);transform:scaleX(0);}25%{-webkit-transform:scaleX(0);-ms-transform:scaleX(0);transform:scaleX(0);}50%{-webkit-transform:scaleX(1);-ms-transform:scaleX(1);transform:scaleX(1);}75%{-webkit-transform:scaleX(1);-ms-transform:scaleX(1);transform:scaleX(1);}100%{background-color:#FFC400;-webkit-transform:translateX(100%);-ms-transform:translateX(100%);transform:translateX(100%);}}@-webkit-keyframes animation-10x2v5k{0%{stroke-dasharray:1,200;stroke-dashoffset:0;}100%{stroke-dasharray:200,200;stroke-dashoffset:-125px;}}@keyframes animation-10x2v5k{0%{stroke-dasharray:1,200;stroke-dashoffset:0;}100%{stroke-dasharray:200,200;stroke-dashoffset:-125px;}}@-webkit-keyframes animation-98wu2y{0%{stroke-dasharray:0,200;}50%{stroke-dasharray:50,200;}100%{stroke-dasharray:94,200;stroke-dashoffset:-94;}}@keyframes animation-98wu2y{0%{stroke-dasharray:0,200;}50%{stroke-dasharray:50,200;}100%{stroke-dasharray:94,200;stroke-dashoffset:-94;}}@-webkit-keyframes animation-xhy3fo{0%{stroke-dasharray:91,200;stroke-dashoffset:90;}30%{stroke-dasharray:91,200;stroke-dashoffset:54;}50%{stroke-dasharray:91,200;stroke-dashoffset:0;}70%{stroke-dasharray:91,200;stroke-dashoffset:-54;}100%{stroke-dasharray:91,200;stroke-dashoffset:-94;}}@keyframes animation-xhy3fo{0%{stroke-dasharray:91,200;stroke-dashoffset:90;}30%{stroke-dasharray:91,200;stroke-dashoffset:54;}50%{stroke-dasharray:91,200;stroke-dashoffset:0;}70%{stroke-dasharray:91,200;stroke-dashoffset:-54;}100%{stroke-dasharray:91,200;stroke-dashoffset:-94;}}@-webkit-keyframes animation-4dw8w1{from{opacity:0;}to{opacity:0.2;}}@keyframes animation-4dw8w1{from{opacity:0;}to{opacity:0.2;}}@-webkit-keyframes animation-1sj7go5{from{opacity:0.2;}to{opacity:0.1;}}@keyframes animation-1sj7go5{from{opacity:0.2;}to{opacity:0.1;}}@-webkit-keyframes animation-egjdad{from{opacity:0.1;}to{opacity:0;}}@keyframes animation-egjdad{from{opacity:0.1;}to{opacity:0;}}@-webkit-keyframes animation-6l2kgb{from{-webkit-transform:scale(1);-ms-transform:scale(1);transform:scale(1);}to{-webkit-transform:scale(0.95);-ms-transform:scale(0.95);transform:scale(0.95);}}@keyframes animation-6l2kgb{from{-webkit-transform:scale(1);-ms-transform:scale(1);transform:scale(1);}to{-webkit-transform:scale(0.95);-ms-transform:scale(0.95);transform:scale(0.95);}}@-webkit-keyframes animation-1rwki9m{from{-webkit-transform:scale(0.95);-ms-transform:scale(0.95);transform:scale(0.95);}to{-webkit-transform:scale(1.01);-ms-transform:scale(1.01);transform:scale(1.01);}}@keyframes animation-1rwki9m{from{-webkit-transform:scale(0.95);-ms-transform:scale(0.95);transform:scale(0.95);}to{-webkit-transform:scale(1.01);-ms-transform:scale(1.01);transform:scale(1.01);}}@-webkit-keyframes animation-13rn2wn{from{-webkit-transform:scale(1.01);-ms-transform:scale(1.01);transform:scale(1.01);}to{-webkit-transform:scale(1);-ms-transform:scale(1);transform:scale(1);}}@keyframes animation-13rn2wn{from{-webkit-transform:scale(1.01);-ms-transform:scale(1.01);transform:scale(1.01);}to{-webkit-transform:scale(1);-ms-transform:scale(1);transform:scale(1);}}.css-kovptw{background-color:var(--N0,#FFFFFF);position:fixed;left:0;width:100%;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-align-items:center;-webkit-box-align:center;-ms-flex-align:center;align-items:center;height:52px;box-shadow:0 2px 4px 0 rgba(0,0,0,0.12);padding:0px 16px;}@media screen and (min-width:768px){.css-kovptw{top:32px;height:56px;box-shadow:0 2px 6px 0 rgba(0,0,0,0.04);padding:0px 30px;}}.css-1ac3sm7{display:none;}@media screen and (min-width:768px){.css-1ac3sm7{background-image:url(https://assets.tokopedia.net/assets-tokopedia-lite/v2/play/kratos/b7e2b1c8..svg);background-repeat:no-repeat;background-position:center center;width:145px;height:32px;background-size:cover;font-size:0;display:block;}}.css-wz4948{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-align-items:center;-webkit-box-align:center;-ms-flex-align:center;align-items:center;margin-left:-16px;overflow:hidden;}@media screen and (min-width:768px){.css-wz4948{display:none;}}.css-1hb0hhl{width:52px;height:52px;margin:0;padding:0;background-image:url(https://assets.tokopedia.net/assets-tokopedia-lite/v2/play/kratos/221853f5..svg);background-position:center center;background-size:20px;background-repeat:no-repeat;border:none;outline:none;-webkit-flex-shrink:0;-ms-flex-negative:0;flex-shrink:0;background-color:transparent;}.css-1h9tmr9{font-size:1.1428571428571428rem;color:var(--N700,#31353B);-webkit-box-flex:1;-webkit-flex-grow:1;-ms-flex-positive:1;flex-grow:1;font-weight:600;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;height:19px;}.css-i3wblx{display:none;}@media screen and (min-width:768px){.css-i3wblx{height:32px;position:fixed;top:0;left:0;width:100%;border-bottom:1px solid var(--N50,#F3F4F5);background-color:var(--N0,#FFFFFF);padding:0px 30px;-webkit-box-pack:justify;-webkit-justify-content:space-between;-ms-flex-pack:justify;justify-content:space-between;-webkit-align-items:center;-webkit-box-align:center;-ms-flex-align:center;align-items:center;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;}}.css-z048k7 a{color:var(--N200,#9FA6B0);font-size:0.8571428571428571rem;}.css-z048k7 a:hover{color:var(--G500,#03AC0E);}.css-bd18ux{padding:0;margin:0;list-style:none;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;}.css-bd18ux li{margin-left:18px;padding-right:6px;}.css-bd18ux li a{color:var(--N200,#9FA6B0);font-size:0.8571428571428571rem;}.css-bd18ux li a:hover{color:var(--G500,#03AC0E);}.css-1c4ya6b{z-index:40;height:52px;position:relative;}@media screen and (min-width:768px){.css-1c4ya6b{height:88px;}}.css-ngxyrx{width:42px;height:42px;object-fit:contain;display:block;}.css-1inzcwr{padding-top:16px;padding-bottom:16px;box-shadow:0 2px 8px 0 var(--NN1000,rgba(0,0,0,0.12));color:var(--NN1000,rgba(0,0,0,0.8));font-size:0.8571428571428571rem;}.css-6mmbwl{position:-webkit-sticky;position:sticky;top:112px;width:240px;list-style:none;display:block;margin:0;padding:0;border-bottom:1px solid #e0e0e0;margin-right:24px;}.css-6mmbwl li{display:block;padding:6px 15px;border:1px solid #e0e0e0;margin:0px 0px -1px;}.css-6mmbwl li a{font-size:14px;color:var(--N600,#40454C);}.css-6mmbwl li:hover{background-color:rgb(247,247,247);}.css-1uoghlk{max-width:1200px;width:100%;margin:24px auto;}.css-1buceln{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-flex-wrap:wrap;-ms-flex-wrap:wrap;flex-wrap:wrap;margin-left:16px;margin-right:16px;box-sizing:border-box;}.css-wr90yg{padding-left:0;padding-right:0;box-sizing:border-box;display:block;-webkit-flex:0 0 auto;-ms-flex:0 0 auto;flex:0 0 auto;width:auto;max-width:100%;}.css-1lj5r9c{padding-left:0;padding-right:0;box-sizing:border-box;-webkit-box-flex:1;-webkit-flex-grow:1;-ms-flex-positive:1;flex-grow:1;-webkit-flex-basis:0;-ms-flex-preferred-size:0;flex-basis:0;max-width:100%;display:block;}.css-1j306kg-unf-loader-line{background-color:var(--NN100,#E4EBF5);display:block;width:100%;height:8px;border-radius:4px;margin-bottom:16px;background-image:linear-gradient(105.02deg,transparent,var(--NN100,#E4EBF5),var(--NN200,#D6DFEB),var(--NN100,#E4EBF5),transparent);background-size:99% 100%;background-repeat:no-repeat;-webkit-animation:animation-jlk0mk 1300ms linear 1ms infinite backwards;animation:animation-jlk0mk 1300ms linear 1ms infinite backwards;}.css-z2zl1v-unf-loader-line{background-color:var(--NN100,#E4EBF5);display:block;width:80%;height:8px;border-radius:4px;margin-bottom:16px;background-image:linear-gradient(105.02deg,transparent,var(--NN100,#E4EBF5),var(--NN200,#D6DFEB),var(--NN100,#E4EBF5),transparent);background-size:99% 100%;background-repeat:no-repeat;-webkit-animation:animation-jlk0mk 1300ms linear 1ms infinite backwards;animation:animation-jlk0mk 1300ms linear 1ms infinite backwards;}.css-1ggf74l{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-flex-wrap:wrap;-ms-flex-wrap:wrap;flex-wrap:wrap;margin-left:22px;margin-right:22px;box-sizing:border-box;-webkit-align-items:center;-webkit-box-align:center;-ms-flex-align:center;align-items:center;}.css-1705l5b{padding-left:8px;padding-right:8px;box-sizing:border-box;-webkit-flex:0 0 auto;-ms-flex:0 0 auto;flex:0 0 auto;width:auto;max-width:100%;display:block;}.css-192yzj{padding-left:8px;padding-right:8px;box-sizing:border-box;-webkit-box-flex:1;-webkit-flex-grow:1;-ms-flex-positive:1;flex-grow:1;-webkit-flex-basis:0;-ms-flex-preferred-size:0;flex-basis:0;max-width:100%;display:block;}body{overflow-x:hidden;}@media (min-width:768px){div[class*=\"unf-bottomsheet\"],div[class*=\"unf-dialog\"]{max-width:768px;left:calc(50% - 384px);}}html{box-sizing:border-box;}*,*::before,*::after{box-sizing:border-box;}@-webkit-keyframes animation-qscw6i{0%{-webkit-transform:scaleX(0);-ms-transform:scaleX(0);transform:scaleX(0);}100%{-webkit-transform:scaleX(1);-ms-transform:scaleX(1);transform:scaleX(1);}}@keyframes animation-qscw6i{0%{-webkit-transform:scaleX(0);-ms-transform:scaleX(0);transform:scaleX(0);}100%{-webkit-transform:scaleX(1);-ms-transform:scaleX(1);transform:scaleX(1);}}@-webkit-keyframes animation-1tbxokc{0%{-webkit-transform:scale(1.1);-ms-transform:scale(1.1);transform:scale(1.1);opacity:1;}100%{-webkit-transform:scale(1.5);-ms-transform:scale(1.5);transform:scale(1.5);opacity:0;}}@keyframes animation-1tbxokc{0%{-webkit-transform:scale(1.1);-ms-transform:scale(1.1);transform:scale(1.1);opacity:1;}100%{-webkit-transform:scale(1.5);-ms-transform:scale(1.5);transform:scale(1.5);opacity:0;}}@-webkit-keyframes animation-30ado7{0%{background-position:-712px 0;}100%{background-position:712px 0;}}@keyframes animation-30ado7{0%{background-position:-712px 0;}100%{background-position:712px 0;}}</style><style data-emotion=\"\"></style><script type=\"text/javascript\" async=\"\" src=\"./Privacy Policy _ Tokopedia_files/js\" nonce=\"\"></script><script id=\"tkpd-iris-tracker-new\" src=\"./Privacy Policy _ Tokopedia_files/index.646d7885ef17e86248a1.js\" async=\"\"></script><script type=\"text/javascript\" async=\"\" src=\"./Privacy Policy _ Tokopedia_files/analytics.js\" nonce=\"\"></script><meta http-equiv=\"origin-trial\" content=\"A751Xsk4ZW3DVQ8WZng2Dk5s3YzAyqncTzgv+VaE6wavgTY0QHkDvUTET1o7HanhuJO8lgv1Vvc88Ij78W1FIAAAAAB7eyJvcmlnaW4iOiJodHRwczovL3d3dy5nb29nbGV0YWdtYW5hZ2VyLmNvbTo0NDMiLCJmZWF0dXJlIjoiUHJpdmFjeVNhbmRib3hBZHNBUElzIiwiZXhwaXJ5IjoxNjgwNjUyNzk5LCJpc1RoaXJkUGFydHkiOnRydWV9\"><link data-rh=\"true\" rel=\"canonical\" href=\"https://www.tokopedia.com/terms\"><link data-rh=\"true\" rel=\"alternate\" media=\"only screen and (max-width: 640px)\" href=\"https://m.tokopedia.com/terms\"><meta data-rh=\"true\" name=\"description\" content=\"Cek Kebijakan Privasi Tokopedia\"><meta data-rh=\"true\" name=\"robots\" content=\"index, follow\"><meta data-rh=\"true\" property=\"og:title\" content=\"Privacy Policy | Tokopedia\"><meta data-rh=\"true\" property=\"og:description\" content=\"Cek Kebijakan Privasi Tokopedia\"><meta data-rh=\"true\" property=\"og:url\" content=\"https://www.tokopedia.com/terms\"><meta data-rh=\"true\" property=\"og:image\" content=\"https://ecs7.tokopedia.net/img/og_image_default_new.jpg\"><meta data-rh=\"true\" name=\"twitter:card\" content=\"summary_large_image\"><meta data-rh=\"true\" name=\"twitter:site\" content=\"@tokopedia\"><meta data-rh=\"true\" name=\"twitter:creator\" content=\"@tokopedia\"><meta data-rh=\"true\" name=\"twitter:title\" content=\"Privacy Policy | Tokopedia\"><meta data-rh=\"true\" name=\"twitter:description\" content=\"Cek Kebijakan Privasi Tokopedia\"><meta data-rh=\"true\" name=\"twitter:image\" content=\"https://ecs7.tokopedia.net/img/og_image_default_new.jpg\"></head><body><div style=\"color:white;background:#FF0000;position:fixed;top:0;left:0;z-index:9999;text-align:center;font-size:calc(9pt + (100vw / 400));line-height:1.4em;padding:2px 7px;border-bottom-right-radius:8px\"><strong>BETA</strong></div><noscript><div>Website ini memerlukan javascript untuk dapat ditampilkan.</div></noscript><div id=\"__skipper\"><main><nav class=\"css-1c4ya6b\"><div class=\"css-i3wblx\"><div class=\"css-z048k7\"><a href=\"https://www.tokopedia.com/mobile-apps\">Download Tokopedia App</a></div><ul class=\"css-bd18ux\"><li><a href=\"https://www.tokopedia.com/about\">Tentang Tokopedia</a></li><li><a href=\"https://www.tokopedia.com/mitra\">Mitra Tokopedia</a></li><li><a href=\"https://seller.tokopedia.com/edu/mulai-berjualan\">Mulai Berjualan</a></li><li><a href=\"https://www.tokopedia.com/promo\">Promo</a></li><li><a href=\"https://www.tokopedia.com/help\">Pusat Bantuan</a></li></ul></div><div class=\"css-kovptw\"><a href=\"https://www.tokopedia.com/\" class=\"css-1ac3sm7\">Tokopedia</a><div class=\"css-wz4948\"><button aria-label=\"Back Button\" type=\"button\" class=\"css-1hb0hhl\"></button><div class=\"css-1h9tmr9\">Kebijakan Privasi</div></div></div></nav><div class=\"css-1uoghlk\"><div class=\"css-1buceln\"><div class=\"css-wr90yg\"><ul class=\"css-6mmbwl\"><li><a href=\"https://www.tokopedia.com/about\">Tentang Kami</a></li><li><a href=\"https://www.tokopedia.com/privacy\">Kebijakan Privasi</a></li><li><a href=\"https://www.tokopedia.com/terms\">Syarat &amp; Ketentuan</a></li></ul></div><div class=\"css-1lj5r9c\"><div class=\"css-fqjcr5\"><h1>Kebijakan Privasi</h1>\n" +
            "<p>Adanya Kebijakan Privasi ini adalah komitmen nyata dari Tokopedia untuk menghargai dan melindungi setiap data atau informasi pribadi Pengguna situs <a href=\"http://www.tokopedia.com/\" target=\"_self\" style=\"\">www.tokopedia.com</a>, situs-situs turunannya, serta aplikasi gawai Tokopedia (selanjutnya disebut sebagai \"Situs\").&nbsp;</p>\n" +
            "<p></p>\n" +
            "<p>Kebijakan Privasi ini (beserta syarat-syarat penggunaan dari situs Tokopedia sebagaimana tercantum dalam Syarat &amp; Ketentuan  dan informasi lain yang tercantum di Situs) menetapkan dasar atas perolehan, pengumpulan, pengolahan, penganalisisan, penampilan, pengiriman, pembukaan, penyimpanan, perubahan, penghapusan dan/atau segala bentuk pengelolaan yang terkait dengan data atau informasi yang mengidentifikasikan atau dapat digunakan untuk mengidentifikasi Pengguna yang Pengguna berikan kepada Tokopedia atau yang Tokopedia kumpulkan dari Pengguna maupun pihak ketiga (selanjutnya disebut sebagai \"Data Pribadi\").</p>\n" +
            "<p></p>\n" +
            "<p>Dengan mengklik “Daftar” (Register) atau pernyataan serupa yang tersedia di laman pendaftaran Situs, Pengguna menyatakan bahwa setiap Data Pribadi Pengguna merupakan data yang benar dan sah, Pengguna mengakui bahwa ia telah diberitahukan dan memahami ketentuan Kebijakan Privasi ini serta Pengguna memberikan persetujuan kepada Tokopedia untuk memperoleh, mengumpulkan, mengolah, menganalisis, menampilkan, mengirimkan, membuka, menyimpan, mengubah, menghapus, mengelola dan/atau mempergunakan data tersebut untuk tujuan sebagaimana tercantum dalam Kebijakan Privasi.</p>\n" +
            "<ol>\n" +
            "<li><a href=\"https://www.tokopedia.com/privacy?lang=id#data-pengguna\" target=\"_self\" style=\"\">Perolehan dan Pengumpulan Data Pribadi Pengguna</a></li>\n" +
            "<li><a href=\"https://www.tokopedia.com/privacy?lang=id#penggunaan-data-pribadi\" target=\"_self\" style=\"\">Penggunaan Data Pribadi</a></li>\n" +
            "<li><a href=\"https://www.tokopedia.com/privacy?lang=id#data-pribadi-pengguna\" target=\"_self\" style=\"\">Pengungkapan Data Pribadi Pengguna</a></li>\n" +
            "<li><a href=\"https://www.tokopedia.com/privacy?lang=id#cookies\" target=\"_self\" style=\"\"><em>Cookies</em></a></li>\n" +
            "<li><a href=\"https://www.tokopedia.com/privacy?lang=id#pengguna-transparansi\" target=\"_self\" style=\"\">Pilihan Pengguna dan Transparansi</a></li>\n" +
            "<li><a href=\"https://www.tokopedia.com/privacy?lang=id#keamanan-data\" target=\"_self\" style=\"\">Keamanan, Penyimpanan dan Penghapusan Data Pribadi Pengguna</a></li>\n" +
            "<li><a href=\"https://www.tokopedia.com/privacy?lang=id#akses-data\" target=\"_self\" style=\"\">Akses dan Perbaikan Data Pribadi Pengguna atau Penarikan Kembali Persetujuan</a></li>\n" +
            "<li><a href=\"https://www.tokopedia.com/privacy?lang=id#pengaduan-data\" target=\"_self\" style=\"\">Pengaduan terkait Perlindungan Data Pribadi Pengguna</a>&nbsp;</li>\n" +
            "<li><a href=\"https://www.tokopedia.com/privacy?lang=id#hubungi-kami\" target=\"_self\" style=\"\">Hubungi Kami</a></li>\n" +
            "<li><a href=\"https://www.tokopedia.com/privacy?lang=id#penyimpanan-penghapusan-informasi\" target=\"_self\" style=\"\">Penyimpanan, Permohonan Subjek Data dan Penghapusan Informasi</a></li>\n" +
            "<li><a href=\"https://www.tokopedia.com/privacy?lang=id#pembaharuan-kebijakan-privasi\" target=\"_self\" style=\"\">Pembaruan Kebijakan Privasi</a></li>\n" +
            "</ol>\n" +
            "<p></p>\n" +
            "<h2><span id=\"data-pengguna\" style=\"\">A. Perolehan dan Pengumpulan Data Pribadi Pengguna</span></h2>\n" +
            "<p>Tokopedia mengumpulkan Data Pribadi Pengguna dengan tujuan untuk memproses transaksi Pengguna, mengelola dan memperlancar proses penggunaan Situs, serta tujuan-tujuan lainnya selama diizinkan oleh peraturan perundang-undangan yang berlaku. Adapun data Pengguna yang dikumpulkan adalah sebagai berikut:</p>\n" +
            "<ol>\n" +
            "<li>Data yang diserahkan secara mandiri oleh Pengguna, termasuk namun tidak terbatas pada data yang diserahkan pada saat Pengguna:</li>\n" +
            "<ol>\n" +
            "<li>membuat atau memperbarui akun Tokopedia, termasuk diantaranya nama pengguna (username), alamat email, nomor telepon, password, alamat, foto, dan informasi lainnya yang dapat mengidentifikasi Pengguna;</li>\n" +
            "<li>melakukan verifikasi toko, termasuk diantaranya Kartu Tanda Penduduk (KTP) dan/atau Nomor Pokok Wajib Pajak (NPWP).</li>\n" +
            "<li>menghubungi Tokopedia, termasuk melalui layanan pelanggan (customer service);</li>\n" +
            "<li>mengisi survei yang dikirimkan oleh Tokopedia atau pihak lain yang ditunjuk secara resmi untuk mewakili Tokopedia;</li>\n" +
            "<li>melakukan interaksi dengan Pengguna lainnya melalui fitur pesan, diskusi produk, ulasan, rating, Pusat Resolusi (sebagaimana didefinisikan dalam Syarat dan Ketentuan) dan fitur interaktif (baik satu atau dua arah) lainnya yang terdapat dalam Situs;</li>\n" +
            "<li>mempergunakan layanan-layanan pada Situs, termasuk data transaksi yang detil, diantaranya jenis, jumlah dan/atau keterangan dari produk atau layanan yang dibeli, alamat pengiriman, kanal pembayaran yang digunakan, jumlah transaksi, tanggal dan waktu transaksi, serta detil transaksi lainnya;</li>\n" +
            "<li>mengisi data-data pembayaran pada saat Pengguna melakukan aktivitas transaksi pembayaran melalui Situs, termasuk namun tidak terbatas pada data rekening bank, kartu kredit, virtual account, instant payment, internet banking, gerai ritel; dan/atau</li>\n" +
            "<li>mengisi data-data detail mengenai alamat pengiriman (untuk Pembeli), alamat penjemputan dan lokasi toko (untuk Penjual), termasuk namun tidak terbatas pada data alamat lengkap, titik koordinat lokasi berupa longitude latitude, nomor telepon, dan nama yang tercantum saat melakukan penyimpanan data di Tokopedia.</li>\n" +
            "<li>menggunakan fitur pada Situs yang membutuhkan izin akses ke data yang relevan yang tersimpan dalam perangkat Pengguna.</li>\n" +
            "</ol>\n" +
            "<li>Data yang terekam pada saat Pengguna mempergunakan Situs, termasuk namun tidak terbatas pada:</li>\n" +
            "<ol>\n" +
            "<li>data lokasi riil atau perkiraannya seperti alamat IP, lokasi Wi-Fi dan geo-location;</li>\n" +
            "<li>data berupa waktu dari setiap aktivitas Pengguna sehubungan dengan penggunaan Situs, termasuk waktu pendaftaran, login dan transaksi;</li>\n" +
            "<li>data penggunaan atau preferensi Pengguna, diantaranya interaksi Pengguna dalam menggunakan Situs, pilihan yang disimpan, serta pengaturan yang dipilih. Data tersebut diperoleh menggunakan cookies, pixel tags, dan teknologi serupa yang menciptakan dan mempertahankan pengenal unik;</li>\n" +
            "<li>data perangkat, diantaranya jenis perangkat yang digunakan untuk mengakses Situs, termasuk model perangkat keras, sistem operasi dan versinya, perangkat lunak, nomor IMEI, nama file dan versinya, pilihan bahasa, pengenal perangkat unik, pengenal iklan, nomor seri, informasi gerakan perangkat, dan/atau informasi jaringan seluler; dan/atau</li>\n" +
            "<li>data catatan (log), diantaranya catatan pada server yang menerima data seperti alamat IP perangkat, tanggal dan waktu akses, fitur aplikasi atau laman yang dilihat, proses kerja aplikasi dan aktivitas sistem lainnya, jenis peramban (browser), dan/atau situs atau layanan pihak ketiga yang Pengguna gunakan sebelum berinteraksi dengan Situs.</li>\n" +
            "</ol>\n" +
            "<li>Data yang diperoleh dari sumber lain, termasuk namun tidak terbatas pada:</li>\n" +
            "<ol>\n" +
            "<li>data berupa geo-location (GPS) dari mitra usaha Tokopedia yang turut membantu Tokopedia dalam mengembangkan dan menyajikan layanan-layanan dalam Situs kepada Pengguna, antara lain mitra penyedia layanan pembayaran, logistik atau kurir, infrastruktur situs, dan mitra- mitra lainnya.</li>\n" +
            "<li>data berupa email, nomor telepon, nama, gender, dan/atau tanggal lahir dari mitra usaha Tokopedia tempat Pengguna membuat atau mengakses akun Tokopedia, seperti layanan media sosial, atau situs/aplikasi yang menggunakan application programming interface (API) Tokopedia atau yang digunakan Tokopedia;</li>\n" +
            "<li>data dari penyedia layanan finansial, termasuk namun tidak terbatas pada lembaga atau biro pemeringkat kredit atau Lembaga Pengelola Informasi Perkreditan (LPIP);</li>\n" +
            "<li>data dari penyedia layanan finansial (apabila Pengguna menggunakan fitur spesifik seperti mengajukan pinjaman melalui Situs/Aplikasi Tokopedia); dan/atau</li>\n" +
            "<li>data berupa email dari penyedia layanan pemasaran.</li>\n" +
            "</ol>\n" +
            "</ol>\n" +
            "<p>Tokopedia dapat menggabungkan data yang diperoleh dari sumber tersebut dengan data lain yang dimilikinya.</p>\n" +
            "<p></p>\n" +
            "<p><a href=\"https://www.tokopedia.com/privacy?lang=id#\" class=\"wyswyg-button\" target=\"_self\" style=\"undefined\"><b>Kembali ke atas</b></a></p>\n" +
            "<p></p>\n" +
            "<p></p>\n" +
            "<h2><span id=\"penggunaan-data-pribadi\" style=\"\">B. Penggunaan Data Pribadi</span></h2>\n" +
            "<p>Tokopedia dapat menggunakan Data Pribadi yang diperoleh dan dikumpulkan dari Pengguna sebagaimana disebutkan dalam bagian sebelumnya untuk hal-hal sebagai berikut:</p>\n" +
            "<ol>\n" +
            "<li>Memproses segala bentuk permintaan, aktivitas maupun transaksi yang dilakukan oleh Pengguna melalui Situs, termasuk untuk keperluan pengiriman produk kepada Pengguna.</li>\n" +
            "<li>Penyediaan fitur-fitur untuk memberikan, mewujudkan, memelihara dan memperbaiki produk dan layanan kami, termasuk:</li>\n" +
            "<ol>\n" +
            "<li>menawarkan, memperoleh, menyediakan, atau memfasilitasi layanan marketplace seperti laman beranda, penelusuran, pencarian, iklan TopAds, rekomendasi produk, Tokopedia Feed, asuransi, pembiayaan, pinjaman, maupun produk-produk lainnya melalui Situs;</li>\n" +
            "<li>memungkinkan fitur untuk mempribadikan (personalised) akun Tokopedia Pengguna, seperti Keranjang Belanja, Wishlist dan Toko Favorit; dan/atau</li>\n" +
            "<li>melakukan kegiatan internal yang diperlukan untuk menyediakan layanan pada situs/aplikasi Tokopedia, seperti pemecahan masalah software, bug, permasalahan operasional, melakukan analisis data, pengujian, dan penelitian, dan untuk memantau dan menganalisis kecenderungan penggunaan dan aktivitas.</li>\n" +
            "</ol>\n" +
            "<li>Membantu Pengguna pada saat berkomunikasi dengan Layanan Pelanggan Tokopedia, diantaranya untuk:</li>\n" +
            "<ol>\n" +
            "<li>memeriksa dan mengatasi permasalahan Pengguna;</li>\n" +
            "<li>mengarahkan pertanyaan Pengguna kepada petugas layanan pelanggan yang tepat untuk mengatasi permasalahan;&nbsp;</li>\n" +
            "<li>mengawasi dan memperbaiki tanggapan layanan pelanggan Tokopedia;</li>\n" +
            "<li>menghubungi Pengguna melalui email, surat, telepon, fax, dan metode komunikasi lainnya, termasuk namun tidak terbatas, untuk membantu dan/atau menyelesaikan proses transaksi maupun proses penyelesaian kendala, serta menyampaikan berita atau notifikasi lainnya sehubungan dengan perlindungan Data Pribadi Pengguna oleh Tokopedia, termasuk kegagalan perlindungan Data Pribadi Pengguna;</li>\n" +
            "<li>menggunakan informasi yang diperoleh dari Pengguna untuk tujuan penelitian, analisis, pengembangan dan pengujian produk guna meningkatkan keamanan layanan-layanan pada Situs, serta mengembangkan fitur dan produk baru; dan</li>\n" +
            "<li>menginformasikan kepada Pengguna terkait produk, layanan, promosi, studi, survei, berita, perkembangan terbaru, acara dan informasi lainnya, baik melalui Situs maupun melalui media lainnya. Tokopedia juga dapat menggunakan informasi tersebut untuk mempromosikan dan memproses kontes dan undian, memberikan hadiah, serta menyajikan iklan dan konten yang relevan tentang layanan Tokopedia dan mitra usahanya.</li>\n" +
            "</ol>\n" +
            "<li>Melakukan <em>monitoring</em> ataupun investigasi terhadap transaksi-transaksi mencurigakan atau transaksi yang terindikasi mengandung unsur kecurangan atau pelanggaran terhadap Syarat dan Ketentuan atau ketentuan hukum yang berlaku, serta melakukan tindakan-tindakan yang diperlukan sebagai tindak lanjut dari hasil monitoring atau investigasi transaksi tersebut.</li>\n" +
            "<li>Dalam keadaan tertentu, Tokopedia mungkin perlu untuk menggunakan ataupun mengungkapkan Data Pribadi Pengguna untuk tujuan penegakan hukum atau untuk pemenuhan persyaratan dan kewajiban peraturan perundang-undangan yang berlaku, termasuk dalam hal terjadinya sengketa atau proses hukum antara Pengguna dan Tokopedia, atau dugaan tindak pidana seperti penipuan atau pencurian data.</li>\n" +
            "<li>Pemenuhan persyaratan dan kewajiban peraturan perundang-undangan yang berlaku sehubungan dengan kepentingan perpajakan di Indonesia.</li>\n" +
            "<li>Memfasilitasi transaksi penggabungan, penjualan aset perusahaan, konsolidasi atau restrukturisasi, pembiayaan atau akuisisi yang melibatkan Tokopedia.</li>\n" +
            "</ol>\n" +
            "<p><a href=\"https://www.tokopedia.com/privacy?lang=id#\" class=\"wyswyg-button\" target=\"_self\" style=\"undefined\"><b>Kembali ke atas</b></a></p>\n" +
            "<p></p>\n" +
            "<p></p>\n" +
            "<h2><span id=\"data-pribadi-pengguna\" style=\"\">C. Pengungkapan Data Pribadi Pengguna</span></h2>\n" +
            "<ol>\n" +
            "<li>Tokopedia berkomitmen untuk menjaga kerahasiaan Data Pribadi Pengguna yang berada di bawah kendali Tokopedia dan menjamin tidak ada pengungkapan, penjualan, pengalihan, distribusi dan/atau peminjaman Data Pribadi Pengguna kepada pihak ketiga lain, tanpa persetujuan dari Pengguna, kecuali dalam hal-hal sebagai berikut:&nbsp;</li>\n" +
            "<ol>\n" +
            "<li>dibutuhkan adanya pengungkapan Data Pribadi Pengguna kepada mitra atau pihak ketiga lain yang membantu Tokopedia dalam menyajikan layanan yang tersedia atau layanan yang di kemudian hari akan tersedia pada Situs dan memproses segala bentuk aktivitas Pengguna dalam Situs, termasuk memproses transaksi, verifikasi pembayaran, promosi, dan pengiriman produk.</li>\n" +
            "<li>Tokopedia dapat menyediakan Data Pribadi Pengguna kepada mitra usaha sesuai dengan persetujuan Pengguna untuk menggunakan layanan mitra usaha, termasuk diantaranya aplikasi atau situs lain yang telah saling mengintegrasikan API atau layanannya, atau mitra usaha yang mana Tokopedia telah bekerjasama untuk menyelenggarakan promosi, perlombaan, atau layanan khusus yang tersedia pada Situs, seperti program <a href=\"https://www.tokopedia.com/help/article/program-tokopedia-tukar-tambah\" target=\"_self\" style=\"\">Tukar Tambah</a> atau <a href=\"https://www.tokopedia.com/help/article/faq-seputar-langsung-laku\" target=\"_self\" style=\"\">Langsung Laku</a> yang diselenggarakan Tokopedia.&nbsp;</li>\n" +
            "<li>Tokopedia dapat menyediakan Data Pribadi Pengguna kepada pihak ketiga yang menggunakan dan mengintegrasikan API publik yang disediakan oleh Tokopedia (termasuk namun tidak terbatas pada penyedia layanan mitra usaha Tokopedia) dengan aplikasi atau situs yang dioperasikannya untuk kepentingan penyelesaian transaksi antara Pembeli dan Penjual pada Situs atau tujuan penggunaan Data Pribadi lainnya yang telah disebutkan pada Bagian B Kebijakan Privasi ini.&nbsp;</li>\n" +
            "<li>dibutuhkan adanya komunikasi antara mitra usaha Tokopedia (seperti penyedia logistik, pembayaran, dan penyedia fitur atau fasilitas layanan lainnya) dengan Pengguna dalam hal penyelesaian kendala maupun hal-hal lainnya.</li>\n" +
            "<li>Tokopedia dapat menyediakan Data Pribadi Pengguna kepada vendor, konsultan, mitra pemasaran, firma/lembaga riset, atau penyedia layanan sejenis dalam rangka kegiatan pemasaran yang dilakukan oleh pihak ketiga, peningkatan dan pemeliharan kualitas layanan Tokopedia, serta kegiatan publikasi lainnya.</li>\n" +
            "<li>Pengguna menghubungi Tokopedia melalui media publik seperti blog, media sosial, dan fitur tertentu pada Situs, yang mana komunikasi antara Pengguna dan Tokopedia tersebut dapat dilihat dan diketahui oleh khalayak ramai.</li>\n" +
            "<li>Tokopedia dapat membagikan Data Pribadi Pengguna kepada anak perusahaan dan afiliasinya untuk membantu memberikan layanan atau melakukan pengolahan data untuk dan atas nama Tokopedia.</li>\n" +
            "<li>Tokopedia dapat mengungkapkan dan/atau memberikan Data Pribadi Pengguna kepada pihak-pihak yang berkepentingan dalam rangka pelaksanaan analisis kelayakan kredit Pengguna.&nbsp;</li>\n" +
            "<li>Tokopedia mengungkapkan Data Pribadi Pengguna dalam upaya mematuhi kewajiban hukum dan/atau adanya permintaan yang sah dari aparat penegak hukum atau instansi penyelenggara negara yang berwenang.</li>\n" +
            "</ol>\n" +
            "<li>Sehubungan dengan pengungkapan Data Pribadi Pengguna sebagaimana dijelaskan di atas, Tokopedia akan/mungkin perlu mengungkapkan Data Pribadi Pengguna kepada penyedia layanan pihak ketiga, agen dan/atau afiliasi atau perusahaan terkait Tokopedia, dan/atau pihak ketiga lainnya yang berlokasi di luar wilayah Indonesia. Meskipun demikian, penyedia layanan pihak ketiga, agen dan/atau afiliasi atau perusahaan terkait dan/atau pihak ketiga lainnya tersebut hanya akan mengelola dan/atau memanfaatkan Data Pribadi Pengguna sehubungan dengan satu atau lebih tujuan sebagaimana diatur dalam Kebijakan Privasi ini dan sesuai dengan ketentuan peraturan perundangan-undangan yang berlaku.</li>\n" +
            "</ol>\n" +
            "<p><a href=\"https://www.tokopedia.com/privacy?lang=id#\" class=\"wyswyg-button\" target=\"_self\" style=\"undefined\"><b>Kembali ke atas</b></a></p>\n" +
            "<p></p>\n" +
            "<p></p>\n" +
            "<h2><span id=\"cookies\" style=\"\">D. Cookies</span></h2>\n" +
            "<ol>\n" +
            "<li>Cookies adalah file kecil yang secara otomatis akan mengambil tempat di dalam perangkat Pengguna yang menjalankan fungsi dalam menyimpan preferensi maupun konfigurasi Pengguna selama mengunjungi suatu situs.</li>\n" +
            "<li>Cookies tersebut tidak diperuntukkan untuk digunakan pada saat melakukan akses data lain yang Pengguna miliki di perangkat komputer Pengguna, selain dari yang telah Pengguna setujui untuk disampaikan.</li>\n" +
            "<li>walaupun secara otomatis perangkat komputer Pengguna akan menerima cookies, Pengguna dapat menentukan pilihan untuk melakukan modifikasi melalui pengaturan browser Pengguna yaitu dengan memilih untuk menolak cookies (pilihan ini dapat menghambat tersedianya layanan Tokopedia secara optimal pada saat Pengguna mengakses Situs).</li>\n" +
            "<li>Tokopedia menggunakan fitur Google Analytics Demographics and Interest. Data yang Tokopedia peroleh dari fitur tersebut, seperti umur, jenis kelamin, minat Pengguna dan informasi lainnya yang dapat mengidentifikasi Pengguna, akan Tokopedia gunakan untuk pengembangan fitur, fasilitas, dan/atau konten yang terdapat pada Situs Tokopedia. Jika Pengguna tidak ingin data miliknya terlacak oleh Google Analytics, Pengguna dapat menggunakan Add-On Google Analytics Opt-Out Browser.</li>\n" +
            "<li>Tokopedia dapat menggunakan fitur-fitur yang disediakan oleh pihak ketiga dalam rangka meningkatkan layanan dan konten Tokopedia, termasuk diantaranya ialah penilaian, penyesuaian, dan penyajian iklan kepada setiap Pengguna berdasarkan minat atau riwayat kunjungan. Jika Pengguna tidak ingin iklan ditampilkan berdasarkan penyesuaian tersebut, maka Pengguna dapat mengaturnya melalui browser.</li>\n" +
            "</ol>\n" +
            "<p><a href=\"https://www.tokopedia.com/privacy?lang=id#\" class=\"wyswyg-button\" target=\"_self\" style=\"undefined\"><b>Kembali ke atas</b></a></p>\n" +
            "<p></p>\n" +
            "<p></p>\n" +
            "<h2><span id=\"pengguna-transparansi\" style=\"\">E. Pilihan Pengguna dan Transparansi</span></h2>\n" +
            "<ol>\n" +
            "<li>Perangkat seluler pada umumnya (iOS dan Android) memiliki pengaturan sehingga aplikasi Tokopedia tidak dapat mengakses data tertentu tanpa persetujuan dari Pengguna. Perangkat iOS akan memberikan pemberitahuan kepada Pengguna saat aplikasi Tokopedia pertama kali meminta akses terhadap data tersebut, sedangkan perangkat Android akan memberikan pemberitahuan kepada Pengguna saat aplikasi Tokopedia pertama kali dimuat. Dengan menggunakan aplikasi dan memberikan akses terhadap aplikasi, Pengguna dianggap memberikan persetujuannya terhadap pengumpulan dan penggunaan Data Pribadi Pengguna dalam perangkatnya.</li>\n" +
            "<li>Setelah transaksi jual beli melalui Situs berhasil, Penjual maupun Pembeli (sebagaimana didefinisikan dalam Syarat dan Ketentuan) memiliki kesempatan untuk memberikan penilaian dan ulasan terhadap satu sama lain. Informasi ini mungkin dapat dilihat secara publik dengan persetujuan Pengguna.</li>\n" +
            "<li>Pengguna dapat mengakses dan mengubah informasi berupa alamat email, nomor telepon, tanggal lahir, jenis kelamin, daftar alamat, metode pembayaran, dan rekening bank melalui fitur pengaturan (settings) pada Situs.</li>\n" +
            "<li>Pengguna dapat menarik diri (opt-out) dari informasi atau notifikasi berupa buletin, ulasan, diskusi produk, pesan pribadi, atau pesan pribadi dari Admin yang dikirimkan oleh Tokopedia melalui fitur pengaturan pada Situs. Tokopedia tetap dapat mengirimkan pesan atau email berupa keterangan transaksi atau informasi terkait akun Pengguna.</li>\n" +
            "<li>Sepanjang tidak bertentangan dengan ketentuan peraturan perundang-undangan yang berlaku, Pengguna dapat menghubungi Tokopedia untuk melakukan penarikan persetujuan terhadap perolehan, pengumpulan, penyimpanan, pengelolaan dan penggunaan data Pengguna. Apabila terjadi demikian maka Pengguna memahami konsekuensi bahwa Pengguna tidak dapat menggunakan layanan Situs maupun layanan Tokopedia lainnya.</li>\n" +
            "</ol>\n" +
            "<p><a href=\"https://www.tokopedia.com/privacy?lang=id#\" class=\"wyswyg-button\" target=\"_self\" style=\"undefined\"><b>Kembali ke atas</b></a></p>\n" +
            "<p></p>\n" +
            "<p></p>\n" +
            "<h2><span id=\"keamanan-data\" style=\"\">F. Keamanan, Penyimpanan dan Penghapusan Data Pribadi Pengguna</span></h2>\n" +
            "<ol>\n" +
            "<li>Tokopedia melindungi setiap Data Pribadi Pengguna yang disimpan dalam sistemnya, serta melindungi data tersebut dari akses, penggunaan, modifikasi, pengambilan dan/atau pengungkapan tidak sah dengan menggunakan sejumlah tindakan dan prosedur keamanan, termasuk kata sandi dan kode OTP (One Time Password) Pengguna.</li>\n" +
            "<li>Data Pribadi Pengguna juga dapat disimpan atau diproses di luar negara oleh pihak yang bekerja untuk Tokopedia di negara lain, atau oleh penyedia layanan pihak ketiga, vendor, pemasok, mitra, kontraktor, atau afiliasi Tokopedia. Dalam hal tersebut, Tokopedia akan memastikan bahwa Data Pribadi tersebut tetap terlindungi sesuai dengan komitmen Tokopedia dalam Kebijakan Privasi ini.</li>\n" +
            "<li>Walaupun Tokopedia telah menggunakan upaya terbaiknya untuk mengamankan dan melindungi Data Pribadi Pengguna, perlu diketahui bahwa pengiriman data melalui Internet tidak pernah sepenuhnya aman. Dengan demikian, Tokopedia tidak dapat menjamin 100% keamanan data yang disediakan atau dikirimkan kepada Tokopedia oleh Pengguna dan pemberian informasi oleh Pengguna merupakan risiko yang ditanggung oleh Pengguna sendiri.</li>\n" +
            "<li>Tokopedia akan menghapus dan/atau menganonimkan Data Pribadi Pengguna yang ada di bawah kendali Tokopedia apabila (i) Data Pribadi Pengguna tidak lagi diperlukan untuk memenuhi tujuan dari pengumpulannya; dan (ii) penyimpanan tidak lagi diperlukan untuk tujuan kepatuhan terhadap peraturan perundang-undangan yang berlaku.&nbsp;</li>\n" +
            "<li>Mohon diperhatikan bahwa masih ada kemungkinan bahwa beberapa Data Pribadi Pengguna disimpan oleh pihak lain, termasuk instansi penyelenggara negara yang berwenang. Dalam hal kami membagikan Data Pribadi Pengguna kepada instansi penyelenggara negara yang berwenang dan/atau instansi lainnya yang dapat ditunjuk oleh pemerintah yang berwenang atau memiliki kerja sama dengan Tokopedia, Pengguna menyetujui dan mengakui bahwa penyimpanan Data Pribadi Pengguna oleh instansi tersebut akan mengikuti kebijakan penyimpanan data masing-masing instansi tersebut.&nbsp;</li>\n" +
            "</ol>\n" +
            "<p><a href=\"https://www.tokopedia.com/privacy?lang=id#\" class=\"wyswyg-button\" target=\"_self\" style=\"undefined\"><b>Kembali ke atas</b></a></p>\n" +
            "<p></p>\n" +
            "<p></p>\n" +
            "<h2><span id=\"akses-data\" style=\"\">G. Akses dan Perbaikan Data Pribadi Pengguna atau Penarikan Kembali Persetujuan</span></h2>\n" +
            "<ol>\n" +
            "<li>Tokopedia mengambil langkah-langkah yang wajar untuk memastikan bahwa Data Pribadi Pengguna diproses secara akurat dan lengkap. Namun demikian, penting bagi Pengguna untuk memberi tahu Tokopedia secara tepat waktu tentang segala perubahan pada Data Pribadi Pengguna atau jika ada kesalahan dalam Data Pribadi Pengguna yang berada di bawah kendali Tokopedia.</li>\n" +
            "<li>Pengguna berhak untuk mengakses atau mengoreksi Data Pribadi Pengguna yang berada di bawah kendali Tokopedia melalui layanan pelanggan Tokopedia yang tersedia dalam Kebijakan Privasi ini. Namun demikian, permohonan tersebut hanya akan diproses oleh Tokopedia apabila Pengguna telah menyerahkan bukti identitas yang memadai untuk melakukan akses atau koreksi terhadap data tersebut. Tokopedia berhak menolak permohonan untuk mengakses, atau untuk memperbaiki, sebagian atau semua Data Pribadi Pengguna yang Tokopedia miliki atau kuasai jika diizinkan atau diperlukan berdasarkan perundang-undangan yang berlaku. Hal ini termasuk dalam keadaan di mana Data Pribadi tersebut dapat berisi referensi kepada orang lain atau di mana permintaan untuk akses atau permintaan untuk mengoreksi adalah untuk alasan yang Tokopedia anggap tidak relevan, tidak serius, atau menyulitkan.</li>\n" +
            "<li>Tokopedia dapat membebankan biaya administrasi kepada Pengguna untuk menangani permintaan Pengguna untuk mengakses atau mengoreksi Data Pribadi Pengguna.&nbsp;</li>\n" +
            "<li>Pengguna dapat menarik persetujuan yang telah Pengguna berikan terkait dengan pemrosesan Data Pribadi Pengguna yang ada di bawah kendali Tokopedia dengan mengirimkan permintaan tersebut melalui layanan pelanggan Tokopedia yang tercantum dalam Kebijakan Privasi ini. Tokopedia akan memproses permintaan Pengguna dalam kurun waktu yang wajar sejak permintaan penarikan persetujuan tersebut disampaikan, dan selanjutnya tidak memproses Data Pribadi Pengguna sesuai dengan permintaan awal Pengguna, kecuali diwajibkan oleh peraturan perundang-undangan yang berlaku. Dalam hal terdapat penarikan persetujuan, Tokopedia mungkin tidak dapat melaksanakan kewajiban-kewajibannya berdasarkan setiap perjanjian antara Pengguna dengan Tokopedia. Sehubungan dengan hal tersebut, terdapat juga kemungkinan bahwa dikarenakan Tokopedia tidak dapat melaksanakan kewajiban-kewajibannya oleh karena Pengguna menarik persetujuannya, setiap hubungan hukum antara Pengguna dan Tokopedia menjadi berakhir dan tidak dapat dilanjutkan.&nbsp;</li>\n" +
            "</ol>\n" +
            "<p><a href=\"https://www.tokopedia.com/privacy?lang=id#\" class=\"wyswyg-button\" target=\"_self\" style=\"undefined\"><b>Kembali ke atas</b></a></p>\n" +
            "<p></p>\n" +
            "<p></p>\n" +
            "<h2><span id=\"pengaduan-data\" style=\"\">H. Pengaduan terkait Perlindungan Data Pengguna </span></h2>\n" +
            "<ol>\n" +
            "<li>Jika Pengguna memiliki kekhawatiran tentang penanganan atau perlakuan Tokopedia terhadap Data Pribadi Pengguna atau jika Pengguna yakin bahwa privasinya telah dilanggar, Pengguna dapat menghubungi Tokopedia melalui layanan pelanggan Tokopedia atau kepada kontak Tokopedia yang tercantum dalam Kebijakan Privasi ini dengan menjelaskan identitas dan sifat keluhan Pengguna.</li>\n" +
            "<li>Tokopedia akan menyelidiki keluhan Pengguna dan berupaya untuk memberikan tanggapan terhadap keluhan tersebut dalam kurun waktu yang wajar setelah menerima keluhan yang disampaikan oleh Pengguna.</li>\n" +
            "</ol>\n" +
            "<p><a href=\"https://www.tokopedia.com/privacy?lang=id#\" class=\"wyswyg-button\" target=\"_self\" style=\"undefined\"><b>Kembali ke atas</b></a></p>\n" +
            "<p></p>\n" +
            "<p></p>\n" +
            "<h2><span id=\"hubungi-kami\" style=\"\">I. Hubungi Kami </span></h2>\n" +
            "<p>Dalam hal Pengguna memiliki pertanyaan, komentar, keluhan, atau klaim mengenai Kebijakan Privasi ini atau Pengguna ingin mendapatkan akses dan/atau melakukan koreksi terhadap Data Pribadi miliknya, silakan hubungi layanan pelanggan Tokopedia melalui laman <a href=\"https://www.tokopedia.com/help\" target=\"_self\" style=\"\">https://www.tokopedia.com/help</a>.</p>\n" +
            "<p><a href=\"https://www.tokopedia.com/privacy?lang=id#\" class=\"wyswyg-button\" target=\"_self\" style=\"undefined\"><b>Kembali ke atas</b></a></p>\n" +
            "<p></p>\n" +
            "<p></p>\n" +
            "<h2><span id=\"penyimpanan-penghapusan-informasi\" style=\"\">J. Penyimpanan, Permohonan Subjek Data dan Penghapusan Informasi</span></h2>\n" +
            "<ol>\n" +
            "<li>Tokopedia akan menyimpan informasi selama akun Pengguna aktif sesuai dengan ketentuan peraturan hukum yang berlaku di Indonesia.</li>\n" +
            "<li>Pengguna memiliki hak untuk mengajukan permohonan subjek data kepada Tokopedia yang selengkapnya dapat dilihat <a href=\"https://www.tokopedia.com/help/article/prosedur-pelaksanaan-permohonan-subjek-data-sebagai-pengguna-tokopedia\" target=\"_blank\" style=\"\">DI SINI</a>.</li>\n" +
            "<li>Pengguna dapat melakukan permohonan penghapusan informasi Pengguna melalui aplikasi Tokopedia dan Tokopedia mobile yang akan dibuka secara berkala yang selengkapnya dapat dilihat <a href=\"https://www.tokopedia.com/help/article/apakah-saya-dapat-menghapus-akun-tokopedia\" target=\"_blank\" style=\"\">DI SINI</a>.&nbsp;</li>\n" +
            "<li>Tokopedia akan memproses permohonan subjek data sesuai dengan ketentuan peraturan hukum yang berlaku di Indonesia.&nbsp;</li>\n" +
            "</ol>\n" +
            "<p></p>\n" +
            "<p><a href=\"https://www.tokopedia.com/privacy?lang=id#\" class=\"wyswyg-button\" target=\"_self\" style=\"undefined\"><b>Kembali ke atas</b></a></p>\n" +
            "<p></p>\n" +
            "<p></p>\n" +
            "<h2><span id=\"pembaharuan-kebijakan-privasi\" style=\"\">K. Pembaruan Kebijakan Privasi</span></h2>\n" +
            "<p>Tokopedia dapat sewaktu-waktu melakukan perubahan atau pembaruan terhadap Kebijakan Privasi ini sebagai bagian dari kepatuhan terhadap peraturan perundang-undangan dan kebijakan internal perusahaan. Tokopedia akan memberikan notifikasi kepada Pengguna dalam adanya perubahan dan/atau pembaruan dalam Kebijakan Privasi ini.&nbsp;</p>\n" +
            "<p></p>\n" +
            "<p><a href=\"https://www.tokopedia.com/privacy?lang=id#\" class=\"wyswyg-button\" target=\"_self\" style=\"undefined\"><b>Kembali ke atas</b></a></p>\n" +
            "</div><span>Last Update: 27/07/2022</span></div></div></div><div data-testid=\"footer\" class=\"css-1inzcwr\"><div class=\"css-1ggf74l\"><div class=\"css-1705l5b\"><img class=\"css-ngxyrx\" src=\"./Privacy Policy _ Tokopedia_files/690c8a72..png\" alt=\"Tokopedia\"></div><div class=\"css-192yzj\">© 2019 - 2022, PT Tokopedia</div></div></div><span style=\"display:none;user-select:none;pointer-events:none\"></span></main></div><script async=\"\" src=\"./Privacy Policy _ Tokopedia_files/branch-latest.min.js\" crossorigin=\"anonymous\"></script><script async=\"\" src=\"./Privacy Policy _ Tokopedia_files/gtm.js\" nonce=\"\"></script><script type=\"text/javascript\" nonce=\"\" data-source=\"skipper\">\n" +
            "          window.NODE_ENV=\"beta\";\n" +
            "          window.version=\"dc11d9115fecba41afccad6e84fd6998634b37de\";\n" +
            "          window.__NR_STATUS=\"false\";\n" +
            "          window.__APP_PUBLIC_RUNTIME__={\"TEST_CONSTANT\":\"public runtime\"};\n" +
            "          window.__APP_STATE__={\"isBot\":false,\"nonce\":\"US\\u002FRHl7sMw0+aYuzeArwaQ==\",\"isDexter\":false,\"isHuawei\":false,\"isLighthouse\":false,\"isMobile\":false,\"isMobileApp\":false,\"isSSG\":false,\"isTkpdApp\":false,\"pageStatus\":200,\"pageType\":\"terms-privacy\",\"tkpdAppName\":\"\",\"userAgent\":\"Mozilla\\u002F5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit\\u002F537.36 (KHTML, like Gecko) Chrome\\u002F107.0.0.0 Safari\\u002F537.36\",\"xdevice\":\"\"};\n" +
            "          window.__BOT=\"false\";\n" +
            "          window.__EMOTION_IDS__=[\"jlk0mk\",\"17fxu0k\",\"3j3ykd\",\"ovhcia\",\"1s16om9\",\"1przuqo\",\"i6183d\",\"1lz1btd\",\"4o66f\",\"10x2v5k\",\"98wu2y\",\"xhy3fo\",\"4dw8w1\",\"1sj7go5\",\"egjdad\",\"6l2kgb\",\"1rwki9m\",\"13rn2wn\",\"kovptw\",\"1ac3sm7\",\"wz4948\",\"1hb0hhl\",\"1h9tmr9\",\"i3wblx\",\"z048k7\",\"bd18ux\",\"1c4ya6b\",\"ngxyrx\",\"1inzcwr\",\"6mmbwl\",\"1uoghlk\",\"1buceln\",\"wr90yg\",\"1lj5r9c\",\"1j306kg-unf-loader-line\",\"z2zl1v-unf-loader-line\",\"1ggf74l\",\"1705l5b\",\"192yzj\",\"n7n7wr\",\"1thpqdv\",\"qscw6i\",\"1tbxokc\",\"30ado7\"];\n" +
            "          window.__ismobileapp=false;\n" +
            "          window.__service=\"play\";\n" +
            "          window.__SHELL=false;\n" +
            "          window.__SKIPPER_SSG=false;\n" +
            "          window.__PAGE_TYPE__=\"terms-privacy\";\n" +
            "          window.__ROUTE_MANIFEST={\"registeredRoutes\":{\"/(terms|privacy)\":{\"gsp\":false,\"gssp\":false},\"/play/channel/:channelID\":{\"gsp\":false,\"gssp\":false},\"/play/channels\":{\"gsp\":false,\"gssp\":false},\"/play/cast-receiver\":{\"gsp\":false,\"gssp\":false},\"/salam\":{\"gsp\":false,\"gssp\":false},\"/play/\":{\"gsp\":false,\"gssp\":false},\"/s/quran/:suratSlug([a-z-_]+)/ayat-(\\\\d+)\":{\"gsp\":false,\"gssp\":false},\"/s/jadwal-sholat/:cityName([a-z-_]+)?/:slugMonthYear([a-z0-9-_]+)?\":{\"gsp\":false,\"gssp\":false},\"/s/quran/:suratSlug([a-z-_]+)/:startAyat([0-9-_]+)?\":{\"gsp\":false,\"gssp\":false},\"/s/notifikasi-salam\":{\"gsp\":false,\"gssp\":false},\"/s/quran/\":{\"gsp\":false,\"gssp\":false},\"/s/quran/search\":{\"gsp\":false,\"gssp\":false},\"/play/live/schedule/create\":{\"gsp\":false,\"gssp\":false},\"/play/live/\":{\"gsp\":false,\"gssp\":false},\"/play/live/:channelID/setup\":{\"gsp\":false,\"gssp\":false}},\"sortedRoutes\":[{\"path\":\"/s\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/salam\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/quran\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/(terms|privacy)\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/live\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/search\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/channels\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/quran/konten\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/quran/search\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/notifikasi-salam\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/testapplink\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/cast-receiver\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/jadwal-sholat/search\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/quran/konten/bookmark\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/:url*(/+)\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/live/schedule/list\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/live/schedule/create\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/quran/:suratSlug([a-z-_]+)\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/live/:channelID\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/shop/:domain\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/channel/:channelID\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/quran/konten/:contentSlug(bookmark|ayat-pilihan|juz-amma|kumpulan-doa)\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/live/:channelID/setup\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/live/:channelID/report\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/live/:channelID/ongoing\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/live/schedule/:channelID\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/quran/:suratSlug([a-z-_]+)/ayat-(\\\\d+)\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/live/schedule/:channelID/edit\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/quran/konten/:contentSlug(bookmark|ayat-pilihan|juz-amma|kumpulan-doa)/:item\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/quran/:suratSlug([a-z-_]+)/:startAyat([0-9-_]+)?\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/shop/:domain/statistic/:channelID?\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/:levelOne(.*-halal)/:levelTwo?/:levelThree?\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/jadwal-sholat/:cityName([a-z-_]+)?/:slugMonthYear([a-z0-9-_]+)?\",\"exact\":true,\"sensitive\":false,\"strict\":false},{\"path\":\"/s/*\",\"exact\":false,\"sensitive\":false,\"strict\":false},{\"path\":\"/play/live/*\",\"exact\":false,\"sensitive\":false,\"strict\":false}]};\n" +
            "          if (typeof window.newrelic !== \"undefined\") {\n" +
            "            window.newrelic.setCustomAttribute(\"env\", window.NODE_ENV);\n" +
            "            window.newrelic.setCustomAttribute(\"isBot\", window.__BOT);\n" +
            "            window.newrelic.setCustomAttribute(\"service\", window.__service);\n" +
            "            window.newrelic.setCustomAttribute(\"version\", window.version);\n" +
            "            window.newrelic.setCustomAttribute(\"sw\", window.__SHELL);\n" +
            "            window.newrelic.setCustomAttribute(\"ssg\", window.__SKIPPER_SSG);\n" +
            "            window.newrelic.setCustomAttribute(\"framework\", \"skipper\");\n" +
            "          }\n" +
            "          </script><script nonce=\"\">window.__APOLLO_STATE__={\"ROOT_QUERY\":{\"__typename\":\"Query\",\"isAuthenticated\":76686370}}</script><noscript><iframe nonce=\"US/RHl7sMw0+aYuzeArwaQ==\" src=\"https://www.googletagmanager.com/ns.html?id=GTM-TZNNXTG\" height=\"0\" width=\"0\" style=\"display:none;visibility:hidden\"></iframe></noscript><script type=\"text/javascript\" nonce=\"\" data-source=\"lite-tracker\" data-name=\"gtm\">(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],j=d.createElement(s),dl=l!=='dataLayer'?'&l='+l:'';j.async=true;j.src='https://www.googletagmanager.com/gtm.js?id='+i+dl;var n=d.querySelector('[nonce]');n&&j.setAttribute('nonce',n.nonce||n.getAttribute('nonce'));f.parentNode.insertBefore(j,f);})(window,document,'script','dataLayer','GTM-TZNNXTG');</script><script type=\"text/javascript\" nonce=\"\" data-source=\"lite-tracker\" data-name=\"branch\">(function(b,r,a,n,c,h,_,s,d,k){if(!b[n]||!b[n]._q){for(;s<_.length;)c(h,_[s++]);d=r.createElement(a);d.async=1;d.src=\"https://cdn.branch.io/branch-latest.min.js\";d.setAttribute('crossorigin','anonymous');k=r.getElementsByTagName(a)[0];k.parentNode.insertBefore(d,k);b[n]=h}})(window,document,\"script\",\"branch\",function(b,r){b[r]=function(){b._q.push([r,arguments])}},{_q:[],_v:1},\"addListener applyCode autoAppIndex banner closeBanner closeJourney creditHistory credits data deepview deepviewCta first getCode init link logout redeem referrals removeListener sendSMS setBranchViewData setIdentity track validateCode trackCommerceEvent logEvent disableTracking\".split(\" \"), 0);branch.init('key_live_abhHgIh1DQiuPxdBNg9EXepdDugwwkHr');</script><script id=\"__LOADABLE_REQUIRED_CHUNKS__\" type=\"application/json\" crossorigin=\"anonymous\" nonce=\"\" defer=\"\">[6620,1175,1222]</script><script id=\"__LOADABLE_REQUIRED_CHUNKS___ext\" type=\"application/json\" crossorigin=\"anonymous\" nonce=\"\" defer=\"\">{\"namedChunks\":[\"routes-terms-privacy\"]}</script><script data-chunk=\"main\" src=\"./Privacy Policy _ Tokopedia_files/runtime.d4bc8a62da703472b33e.js\" crossorigin=\"anonymous\" nonce=\"\" defer=\"\"></script><script data-chunk=\"main\" src=\"./Privacy Policy _ Tokopedia_files/apollo.076d5a8ed52c5684b012.js\" crossorigin=\"anonymous\" nonce=\"\" defer=\"\"></script><script data-chunk=\"main\" src=\"./Privacy Policy _ Tokopedia_files/unify.5b7b3134ed42f54ec842.js\" crossorigin=\"anonymous\" nonce=\"\" defer=\"\"></script><script data-chunk=\"main\" src=\"./Privacy Policy _ Tokopedia_files/framework.e4540b4ca8956a8f8444.js\" crossorigin=\"anonymous\" nonce=\"\" defer=\"\"></script><script data-chunk=\"main\" src=\"./Privacy Policy _ Tokopedia_files/vendor.d5794ffac83a33df3021.js\" crossorigin=\"anonymous\" nonce=\"\" defer=\"\"></script><script data-chunk=\"main\" src=\"./Privacy Policy _ Tokopedia_files/main.4112d8daf3ab972ce2d9.js\" crossorigin=\"anonymous\" nonce=\"\" defer=\"\"></script><script data-chunk=\"routes-terms-privacy\" src=\"./Privacy Policy _ Tokopedia_files/chunk.6620.0cc8117e1ac429bb2d19.js\" crossorigin=\"anonymous\" nonce=\"\" defer=\"\"></script><script data-chunk=\"routes-terms-privacy\" src=\"./Privacy Policy _ Tokopedia_files/chunk.1175.b11962287510b87d9666.js\" crossorigin=\"anonymous\" nonce=\"\" defer=\"\"></script><script data-chunk=\"routes-terms-privacy\" src=\"./Privacy Policy _ Tokopedia_files/chunk.routes-terms-privacy.3ba5f4a53c0051878eac.js\" crossorigin=\"anonymous\" nonce=\"\" defer=\"\"></script>\n" +
            "<script type=\"text/javascript\" id=\"\">!function(b,e,f,g,a,c,d){b.fbq||(a=b.fbq=function(){a.callMethod?a.callMethod.apply(a,arguments):a.queue.push(arguments)},b._fbq||(b._fbq=a),a.push=a,a.loaded=!0,a.version=\"2.0\",a.queue=[],c=e.createElement(f),c.async=!0,c.src=g,d=e.getElementsByTagName(f)[0],d.parentNode.insertBefore(c,d))}(window,document,\"script\",\"//connect.facebook.net/en_US/fbevents.js?v\\x3d2.9.87\");fbq(\"init\",\"1419424518349836\");fbq(\"track\",\"PageView\");</script>\n" +
            "<noscript><img height=\"1\" width=\"1\" style=\"display:none\" src=\"https://www.facebook.com/tr?id=1419424518349836&amp;ev=PageView&amp;noscript=1\"></noscript>\n" +
            "<script type=\"text/javascript\" id=\"\">window.init_iris=!0;function isStaging(){return window&&window.location&&window.location.hostname.includes(\"staging\")}function createScript(c){void 0===c&&(c={});var a=c,e=a.id,f=a.src,g=a.callback,b=a.async;b=void 0===b?!0:b;a=a.defer;a=void 0===a?!1:a;var h=document.getElementsByTagName(\"script\")[0],d=document.createElement(\"script\");d.id=e;d.src=f;d.async=b;d.defer=a;h.parentNode.insertBefore(d,h);g&&\"function\"===typeof g&&(d.onload=g);return c}\n" +
            "function iris(){var c=google_tag_manager[\"GTM-TZNNXTG\"].macro(4),a=function(){var a=\"desktop\",b=\"default_v2\";c&&(a=\"mobile\",b=\"default_mweb\");return window&&window.datalayerClientWeb&&window.datalayerClientWeb.initializeGTMWithIris({container:\"gtm\",device:a,customEventField:\"event_ga\",defaultEventValue:b},\"dataLayer\",google_tag_manager[\"GTM-TZNNXTG\"].macro(5))},e=google_tag_manager[\"GTM-TZNNXTG\"].macro(6),f=isStaging();f&&(e=google_tag_manager[\"GTM-TZNNXTG\"].macro(7));createScript({id:\"tkpd-iris-tracker-new\",src:e,callback:a})}iris();</script><script type=\"application/javascript\" async=\"\" src=\"./Privacy Policy _ Tokopedia_files/f.txt\" nonce=\"\" data-ctrld=\"yes\"></script><script async=\"\" src=\"./Privacy Policy _ Tokopedia_files/fbevents.js\" type=\"application/javascript\" data-ctrld=\"yes\"></script><script src=\"./Privacy Policy _ Tokopedia_files/1419424518349836\" type=\"application/javascript\" async=\"\" data-ctrld=\"yes\"></script></body></html>"
    }
}
