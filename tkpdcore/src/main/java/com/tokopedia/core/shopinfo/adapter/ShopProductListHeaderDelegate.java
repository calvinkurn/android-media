package com.tokopedia.core.shopinfo.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.utils.SimpleSpinnerAdapter;
import com.tokopedia.core.R;
import com.tokopedia.core.shopinfo.models.GetShopProductParam;
import com.tokopedia.core.shopinfo.models.etalasemodel.EtalaseAdapterModel;
import com.tokopedia.core.app.MainApplication;

/**
 * Created by Tkpd_Eka on 11/2/2015.
 */
public class ShopProductListHeaderDelegate {

    public interface ProductHeaderListListener {
        void onToggleView();

        void onEtalaseClick(int pos);

        void onFilterClick(View v);

        void onSpinnerEtalaseClick();
    }

    private ProductHeaderListListener listener;
    private SpinnerInteractionListener spinnerInteractionListener;
    private ArrayAdapter<EtalaseAdapterModel> etalaseAdapter;
    private FeaturedProductAdapter featuredProductAdapter;
    private int spinnerLastPos = 0;
    // if selection == -1, no selection request
    private int spinnerSelectedPos = -1;
    private VHolder vholder;

    public ShopProductListHeaderDelegate() {
        spinnerInteractionListener = new SpinnerInteractionListener();
    }

    private class VHolder extends RecyclerView.ViewHolder {

        public ImageView toggle;
        public Spinner etalase;
        public View filterClick;
        public RecyclerView featuredProductList;
        public TextView featuredProductTitle;
        public TextView productSectionTitle;

        public VHolder(View itemView) {
            super(itemView);
            toggle = (ImageView) itemView.findViewById(R.id.toggle_view);
            filterClick = itemView.findViewById(R.id.btn_filter_sort);
            etalase = (Spinner) itemView.findViewById(R.id.spinner_etalase);
            featuredProductList = (RecyclerView) itemView.findViewById(R.id.featured_product_list);
            featuredProductTitle = (TextView) itemView.findViewById(R.id.featured_product_title);
            productSectionTitle = (TextView) itemView.findViewById(R.id.product_section_title);
        }
    }

    public void setEtalaseAdapter(ArrayAdapter<EtalaseAdapterModel> etalaseAdapter) {
        this.etalaseAdapter = etalaseAdapter;
    }

    public void setFeaturedProductAdapter(FeaturedProductAdapter featuredProductAdapter) {
        this.featuredProductAdapter = featuredProductAdapter;
    }

    public void setSelectedEtalase(int pos) {
        spinnerLastPos = pos;
        spinnerSelectedPos = pos;
        if (listener != null) {
            listener.onEtalaseClick(pos);
        }
    }

    public void setProductListHeader(ProductHeaderListListener listener) {
        this.listener = listener;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_product_list_header, parent, false);
        return new VHolder(view);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int toggleIcon) {
        vholder = (VHolder) holder;
        vholder.toggle.setOnClickListener(onToggleView());
        vholder.filterClick.setOnClickListener(onFilterClick());
        vholder.etalase.setOnItemSelectedListener(spinnerInteractionListener);
        vholder.etalase.setOnTouchListener(spinnerInteractionListener);
        if (vholder.etalase.getAdapter() == null)
            vholder.etalase.setAdapter(etalaseAdapter);
        vholder.toggle.setImageResource(toggleIcon);
        if (hasSelection(spinnerSelectedPos))
            vholder.etalase.setSelection(spinnerSelectedPos);

        if (featuredProductAdapter.getItemCount() > 0 && isAllEtalaseSelected()) {
            vholder.featuredProductTitle.setVisibility(View.VISIBLE);
            vholder.featuredProductList.setVisibility(View.VISIBLE);
            vholder.productSectionTitle.setVisibility(View.VISIBLE);
        } else {
            vholder.featuredProductTitle.setVisibility(View.GONE);
            vholder.featuredProductList.setVisibility(View.GONE);
            vholder.productSectionTitle.setVisibility(View.GONE);
        }

        if (vholder.featuredProductList.getLayoutManager() == null) {
            vholder.featuredProductList.setLayoutManager(
                    new LinearLayoutManager(MainApplication.getAppContext(),
                            LinearLayoutManager.VERTICAL, false)
            );
        }

        if (vholder.featuredProductList.getAdapter() == null) {
            vholder.featuredProductList.setAdapter(featuredProductAdapter);
        }
    }

    private boolean isAllEtalaseSelected() {
        if (spinnerLastPos >= 0) {
            return etalaseAdapter != null &&
                    etalaseAdapter.getCount() > spinnerLastPos &&
                    GetShopProductParam.DEFAULT_ALL_ETALASE_ID.equals(etalaseAdapter.getItem(spinnerLastPos).getEtalaseId());
        } else {
            return false;
        }
    }

    private boolean hasSelection(int spinnerSelectedPos) {
        return spinnerSelectedPos > -1;
    }

    public class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long i) {
            if (userSelect) {
                if (i != spinnerLastPos) {
                    spinnerLastPos = pos;
                    if (listener != null) {
                        listener.onEtalaseClick(pos);
                    }
                }
                userSelect = false;
            }
            if (i == spinnerSelectedPos)
                spinnerSelectedPos = -1;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    private View.OnClickListener onFilterClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFilterClick(view);
            }
        };
    }

    private View.OnClickListener onToggleView() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onToggleView();
            }
        };
    }

}
