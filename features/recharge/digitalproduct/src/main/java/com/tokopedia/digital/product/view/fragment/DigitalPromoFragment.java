package com.tokopedia.digital.product.view.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.adapter.BannerAdapter;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.utils.LinearLayoutManagerNonScroll;
import com.tokopedia.unifycomponents.Toaster;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by furqan on 07/06/18.
 */

public class DigitalPromoFragment extends Fragment implements BannerAdapter.ActionListener {

    private static final String CLIP_DATA_LABEL_VOUCHER_CODE_DIGITAL =
            "CLIP_DATA_LABEL_VOUCHER_CODE_DIGITAL";
    private static final String EXTRA_BANNER_TITLE = "EXTRA_BANNER_TITLE";
    private static final String EXTRA_OTHER_BANNER_TITLE = "EXTRA_OTHER_BANNER_TITLE";
    private static final String EXTRA_BANNER_LIST = "EXTRA_BANNER_LIST";
    private static final String EXTRA_OTHER_BANNER_LIST = "EXTRA_OTHER_BANNER_LIST";

    private RecyclerView rvPromo;

    private BannerAdapter bannerAdapter;

    private String voucherCodeCopiedState;

    private String bannerDataTitle;
    private String otherBannerDataTitle;
    private List<BannerData> bannerDataList;
    private List<BannerData> otherBannerDataList;

    public static DigitalPromoFragment createInstance(DigitalPromoConnector digitalPromoConnector) {
        DigitalPromoFragment fragment = new DigitalPromoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_BANNER_TITLE, digitalPromoConnector.getBannerDataTitle());
        bundle.putParcelableArrayList(EXTRA_BANNER_LIST, (ArrayList<? extends Parcelable>) digitalPromoConnector.getBannerDataList());
        bundle.putString(EXTRA_OTHER_BANNER_TITLE, digitalPromoConnector.getOtherBannerDataTitle());
        bundle.putParcelableArrayList(EXTRA_OTHER_BANNER_LIST, (ArrayList<? extends Parcelable>) digitalPromoConnector.getOtherBannerDataList());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            bannerDataTitle = savedInstanceState.getString(EXTRA_BANNER_TITLE);
            bannerDataList = savedInstanceState.getParcelableArrayList(EXTRA_BANNER_LIST);
            otherBannerDataTitle = savedInstanceState.getString(EXTRA_OTHER_BANNER_TITLE);
            otherBannerDataList = savedInstanceState.getParcelableArrayList(EXTRA_OTHER_BANNER_LIST);
        } else if (getArguments() != null) {
            bannerDataTitle = getArguments().getString(EXTRA_BANNER_TITLE);
            bannerDataList = getArguments().getParcelableArrayList(EXTRA_BANNER_LIST);
            otherBannerDataTitle = getArguments().getString(EXTRA_OTHER_BANNER_TITLE);
            otherBannerDataList = getArguments().getParcelableArrayList(EXTRA_OTHER_BANNER_LIST);
        }
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(EXTRA_BANNER_TITLE, bannerDataTitle);
        outState.putString(EXTRA_OTHER_BANNER_TITLE, otherBannerDataTitle);
        outState.putParcelableArrayList(EXTRA_BANNER_LIST, (ArrayList<? extends Parcelable>) bannerDataList);
        outState.putParcelableArrayList(EXTRA_OTHER_BANNER_LIST, (ArrayList<? extends Parcelable>) otherBannerDataList);
    }

    private void initView() {
        bannerAdapter = new BannerAdapter(getActivity(), this);
        rvPromo.setLayoutManager(new LinearLayoutManagerNonScroll(getActivity()));
        rvPromo.setAdapter(bannerAdapter);
        renderData();
    }

    private void renderData() {
        if (bannerDataTitle != null && bannerDataList != null) {
            renderBannerListData(bannerDataTitle, bannerDataList);
        }
        if (otherBannerDataTitle != null && otherBannerDataList != null) {
            renderOtherBannerListData(otherBannerDataTitle, otherBannerDataList);
        }
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
            RouteManager.route(getActivity(), bannerData.getLink());
        }
    }

    private void showToastMessage(String message) {
        View view = getView();
        if (view != null) {
            Toaster.build(view, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show();
        }
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
