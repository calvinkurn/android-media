package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;

public class SelectDealQuantityFragment extends TkpdBaseV4Fragment implements View.OnClickListener {

    private CategoryItemsViewModel dealDetails;
    private DealFragmentCallbacks fragmentCallbacks;
    private Toolbar toolbar;
    private ImageView imageViewBrand;
    private ImageView imageViewAdd;
    private ImageView imageViewSubtract;
    private TextView textViewdealDetails;
    private TextView textViewbrandName;
    private TextView textViewMrp;
    private TextView textViewSalesPrice;
    private TextView textViewTotalAmount;
    private TextView textViewQuantity;
    private LinearLayout llContinue;
    private int MAX_QUANTITY = 8;
    private int CURRENT_QUANTITY = 1;

    public static Fragment createInstance() {
        Fragment fragment = new SelectDealQuantityFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dealDetails = fragmentCallbacks.getDealDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal_quantity, container, false);
        setHasOptionsMenu(true);
        setViewIds(view);
        initializeViews();
        return view;
    }


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallbacks = (DealDetailsActivity) activity;
    }

    private void setViewIds(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_close_deals));
        toolbar.setTitle(getActivity().getResources().getString(R.string.select_number_of_voucher));
        imageViewBrand = view.findViewById(R.id.imageViewBrand);
        imageViewAdd = view.findViewById(R.id.iv_add);
        imageViewSubtract = view.findViewById(R.id.iv_subtract);
        textViewdealDetails = view.findViewById(R.id.tv_deal_details);
        textViewbrandName = view.findViewById(R.id.tv_brandName);
        textViewQuantity = view.findViewById(R.id.tv_no_quantity);
        textViewMrp = view.findViewById(R.id.tvMrp);
        textViewSalesPrice = view.findViewById(R.id.tvSalesPrice);
        textViewTotalAmount = view.findViewById(R.id.tv_total_amount);
        llContinue = view.findViewById(R.id.ll_continue);

    }

    private void initializeViews() {
        if (dealDetails.getBrand() != null) {
            ImageHandler.loadImage(getContext(), imageViewBrand, dealDetails.getBrand().getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
            textViewbrandName.setText(dealDetails.getBrand().getTitle());
        }

        textViewdealDetails.setText(dealDetails.getDisplayName());

        textViewQuantity.setText(CURRENT_QUANTITY + "");
        textViewMrp.setText(Utils.convertToCurrencyString(dealDetails.getMrp()));
        textViewMrp.setPaintFlags(textViewMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        textViewSalesPrice.setText(Utils.convertToCurrencyString(dealDetails.getSalesPrice()));
        textViewTotalAmount.setText(Utils.convertToCurrencyString(dealDetails.getSalesPrice()));

        llContinue.setOnClickListener(this);
        imageViewAdd.setOnClickListener(this);
        imageViewSubtract.setOnClickListener(this);

    }

    void setButtons() {

        if (CURRENT_QUANTITY > 0) {
            imageViewSubtract.setColorFilter(ContextCompat.getColor(getContext(), R.color.green_nob), android.graphics.PorterDuff.Mode.SRC_IN);
            imageViewSubtract.setClickable(true);

        } else {
            imageViewSubtract.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_400), android.graphics.PorterDuff.Mode.SRC_IN);
            imageViewSubtract.setClickable(false);
        }
        if (CURRENT_QUANTITY < MAX_QUANTITY) {
            imageViewAdd.setColorFilter(ContextCompat.getColor(getContext(), R.color.green_nob), android.graphics.PorterDuff.Mode.SRC_IN);
            imageViewAdd.setClickable(true);

        } else {
            imageViewAdd.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_400), android.graphics.PorterDuff.Mode.SRC_IN);
            imageViewAdd.setClickable(false);
        }
    }


    void setUpTotalAmount() {
        if (CURRENT_QUANTITY > 0) {
            textViewTotalAmount.setVisibility(View.VISIBLE);
            llContinue.setVisibility(View.VISIBLE);
            textViewTotalAmount.setText(Utils.convertToCurrencyString(dealDetails.getSalesPrice() * CURRENT_QUANTITY));
        } else {
            textViewTotalAmount.setVisibility(View.GONE);
            llContinue.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_subtract) {
            if (CURRENT_QUANTITY > 0) {
                CURRENT_QUANTITY--;
                textViewQuantity.setText(String.format(getContext().getResources().getString(R.string.quantity_of_deals), CURRENT_QUANTITY));
            }
            setUpTotalAmount();
            setButtons();

        } else if (v.getId() == R.id.iv_add) {
            if (CURRENT_QUANTITY < MAX_QUANTITY) {
                CURRENT_QUANTITY++;
                textViewQuantity.setText(String.format(getContext().getResources().getString(R.string.quantity_of_deals), CURRENT_QUANTITY));
            }
            setUpTotalAmount();
            setButtons();

        } else if (v.getId() == R.id.ll_continue) {

        }
    }
}
