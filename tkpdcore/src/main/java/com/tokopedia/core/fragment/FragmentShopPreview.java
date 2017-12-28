package com.tokopedia.core.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.facades.GetShopInfoRetrofit;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentShopPreview extends Fragment {
    private static final String ARG_DEEPLINK_INSTANCE = "ARG_DEEPLINK_INSTANCE";
    private boolean isFromDeeplink = false;
    private String ShopDomain;
    private String URL;
    private View MainView;
    private ViewHolder Holder;
    private TkpdProgressDialog progressDialog;
    private DeepLinkWebViewHandleListener webViewHandleListener;
    private GetShopInfoRetrofit getShopInfoRetrofit;

    public static FragmentShopPreview createInstances(String ShopDomain, String URL) {
        FragmentShopPreview fragment = new FragmentShopPreview();
        Bundle bundle = new Bundle();
        bundle.putString("shop_domain", ShopDomain);
        bundle.putString("url", URL);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * This instance used for deeplink, to redirect to web view is shop is not found
     */
    public static FragmentShopPreview createInstanceForDeeplink(String ShopDomain, String URL) {
        FragmentShopPreview fragment = new FragmentShopPreview();
        Bundle bundle = new Bundle();
        bundle.putString("shop_domain", ShopDomain);
        bundle.putString("url", URL);
        bundle.putBoolean(ARG_DEEPLINK_INSTANCE, true);
        fragment.setArguments(bundle);
        return fragment;
    }

    private class ViewHolder {
        LinearLayout rootLayout;
        TextView vShopName;
        ImageView vGoldImg;
        ImageView officialStoreBadgetImageView;
        TextView vShopLoc;
        ImageView vShopAvatar;
        View vShopInfo;
        View visitShop;
    }


    public FragmentShopPreview() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null) {
            ShopDomain = getArguments().getString("shop_domain");
            URL = getArguments().getString("url");
            isFromDeeplink = getArguments().getBoolean(ARG_DEEPLINK_INSTANCE);
        }
        initVar();
    }

    private void initVar() {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.MAIN_PROGRESS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainView = inflater.inflate(R.layout.fragment_fragment_shop_preview, container, false);
        MainView.setVisibility(View.INVISIBLE);
        Holder = new ViewHolder();
        Holder.rootLayout = (LinearLayout) MainView.findViewById(R.id.shop_info);
        Holder.vShopName = (TextView) MainView.findViewById(R.id.shop_name);
        Holder.vGoldImg = (ImageView) MainView.findViewById(R.id.gold_merchant);
        Holder.officialStoreBadgetImageView = (ImageView) MainView.findViewById(R.id.image_official_store);
        Holder.vShopLoc = (TextView) MainView.findViewById(R.id.shop_loc);
        Holder.vShopAvatar = (ImageView) MainView.findViewById(R.id.shop_ava);
        Holder.vShopInfo = MainView.findViewById(R.id.shop_info);
        Holder.visitShop = MainView.findViewById(R.id.visit_shop);
        GetShopInfo();
        setListener();
        return MainView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            webViewHandleListener = (DeepLinkWebViewHandleListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DetailFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        webViewHandleListener = null;
    }

    private void setListener() {
        Holder.rootLayout.setVisibility(View.GONE);
        Holder.visitShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShop();
            }
        });
    }

    private void openShop() {
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        intent.putExtras(ShopInfoActivity.createBundle("", ShopDomain));
        getActivity().startActivity(intent);
    }

    public void GetShopInfo() {
        progressDialog.showDialog();
        getShopInfoRetrofit = new GetShopInfoRetrofit(getActivity(), "", ShopDomain);
        getShopInfoRetrofit.setGetShopInfoListener(new GetShopInfoRetrofit.OnGetShopInfoListener() {
            @Override
            public void onSuccess(String result) {
                progressDialog.dismiss();
                MainView.setVisibility(View.VISIBLE);
                try {
                    ShopModel shopModel = new Gson().fromJson(result, com.tokopedia.core.shopinfo.models.shopmodel.ShopModel.class);
                    if (shopModel.info != null) {
                        try {
                            LoadShopInfoToUI(new JSONObject(result));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            webViewHandleListener.catchToWebView(URL);
                        }
                    } else {
                        webViewHandleListener.catchToWebView(URL);
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    if (webViewHandleListener != null) {
                        webViewHandleListener.catchToWebView(URL);
                    }
                }
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                if (isFromDeeplink) {
                    webViewHandleListener.catchToWebView(URL);
                } else {
                    MainView.setVisibility(View.VISIBLE);
                    NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            GetShopInfo();
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
                if (webViewHandleListener != null) {
                    webViewHandleListener.catchToWebView(URL);
                }
            }
        });
        getShopInfoRetrofit.getShopInfo();
    }

    private void LoadShopInfoToUI(JSONObject Result) throws JSONException {
        JSONObject ShopInfo = new JSONObject(Result.getString("info"));
        Holder.vShopName.setText(MethodChecker.fromHtml(ShopInfo.getString("shop_name")));
        Holder.vShopLoc.setText(ShopInfo.getString("shop_location"));

        ImageHandler.loadImageCircle2(getActivity(), Holder.vShopAvatar, ShopInfo.getString("shop_avatar"));

        int mIsGold = -1;

        if (!ShopInfo.isNull("shop_is_gold")) {
            mIsGold = ShopInfo.getInt("shop_is_gold");
        } else {
            mIsGold = 0;
        }

        int isOfficalStore = -1;

        if (!ShopInfo.isNull("shop_is_official")) {
            isOfficalStore = ShopInfo.getInt("shop_is_official");
        }

        if (mIsGold == 0) {
            Holder.vGoldImg.setVisibility(View.GONE);
        }

        if (isOfficalStore == 1){
            Holder.vGoldImg.setVisibility(View.GONE);
            Holder.officialStoreBadgetImageView.setVisibility(View.VISIBLE);
        }

        Holder.rootLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getShopInfoRetrofit.cancelGetShopInfo();

    }
}
