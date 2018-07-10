package com.tokopedia.digital.product.view.fragment;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.activity.DigitalWebActivity;
import com.tokopedia.digital.product.view.adapter.BannerAdapter;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.utils.LinearLayoutManagerNonScroll;

import java.util.List;

/**
 * @author by furqan on 07/06/18.
 */

public class DigitalPromoFragment extends Fragment implements BannerAdapter.ActionListener {


    private static final String CLIP_DATA_LABEL_VOUCHER_CODE_DIGITAL =
            "CLIP_DATA_LABEL_VOUCHER_CODE_DIGITAL";

    private RecyclerView rvPromo;

    private BannerAdapter bannerAdapter;
    private DigitalPromoConnector digitalPromoConnector;

    private String voucherCodeCopiedState;

    public static DigitalPromoFragment createInstance() {
        return new DigitalPromoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_digital_promo, container, false);
        rvPromo = view.findViewById(R.id.rv_banner);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        renderData();
    }

    public void setDigitalPromoConnector(DigitalPromoConnector digitalPromoConnector) {
        this.digitalPromoConnector = digitalPromoConnector;
    }

    private void initView() {
        bannerAdapter = new BannerAdapter(getActivity(), this);
        rvPromo.setLayoutManager(new LinearLayoutManagerNonScroll(getActivity()));
        rvPromo.setAdapter(bannerAdapter);
    }

    private void renderData() {
        renderBannerListData(digitalPromoConnector.getBannerDataTitle(),
                digitalPromoConnector.getBannerDataList());
        renderOtherBannerListData(digitalPromoConnector.getOtherBannerDataTitle(),
                digitalPromoConnector.getOtherBannerDataList());
    }

    private void renderBannerListData(String title, List<BannerData> bannerDataList) {
        String formattedTitle = getResources().getString(R.string.promo_category, title);
        bannerAdapter.addBannerDataListAndTitle(bannerDataList, formattedTitle);
    }

    private void renderOtherBannerListData(String title, List<BannerData> otherBannerDataList) {
        String formattedTitle = getResources().getString(R.string.promo_category, title);
        bannerAdapter.addBannerDataListAndTitle(otherBannerDataList, formattedTitle);
    }

    @Override
    public void onButtonCopyBannerVoucherCodeClicked(String voucherCode) {
        this.voucherCodeCopiedState = voucherCode;
        ClipboardManager clipboard = (ClipboardManager)
                getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(
                CLIP_DATA_LABEL_VOUCHER_CODE_DIGITAL, voucherCode
        );
        clipboard.setPrimaryClip(clip);
        showToastMessage(getString(R.string.message_voucher_code_banner_copied));
    }

    @Override
    public void onBannerItemClicked(BannerData bannerData) {
        if (!TextUtils.isEmpty(bannerData.getLink())) {
            int deeplinkType = DeepLinkChecker.getDeepLinkType(bannerData.getLink());
            if (deeplinkType == DeepLinkChecker.PROMO) {
                Uri uriData = Uri.parse(bannerData.getLink());
                List<String> linkSegment = uriData.getPathSegments();
                openPromo(bannerData.getLink(), linkSegment);
            } else {
                navigateToActivity(DigitalWebActivity.newInstance(
                        getActivity(), bannerData.getLink())
                );
            }
        }
    }

    private void showToastMessage(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showGreenCloseSnackbar(getActivity(), message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void openPromo(String url, List<String> linkSegment) {
        IDigitalModuleRouter router = ((IDigitalModuleRouter) getActivity().getApplication());
        if (linkSegment.size() == 2) {
            Intent intent = router.getPromoDetailIntent(getActivity(), linkSegment.get(1));
            startActivity(intent);
        } else if (linkSegment.size() == 1) {
            FirebaseRemoteConfigImpl remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
            boolean remoteConfigEnable = remoteConfig.getBoolean(
                    TkpdCache.RemoteConfigKey.MAINAPP_NATIVE_PROMO_LIST
            );
            if (remoteConfigEnable) {
                Intent intent = router.getPromoListIntent(getActivity());
                startActivity(intent);
            } else {
                navigateToActivity(DigitalWebActivity.newInstance(
                        getActivity(), url)
                );
            }
        }
    }

    private void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    public interface DigitalPromoConnector {
        String getBannerDataTitle();

        List<BannerData> getBannerDataList();

        String getOtherBannerDataTitle();

        List<BannerData> getOtherBannerDataList();
    }
}
