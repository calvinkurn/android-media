package com.tokopedia.tkpd.shopinfo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.tkpd.library.utils.SimpleSpinnerAdapter;
import com.tokopedia.tkpd.R;

/**
 * Created by Tkpd_Eka on 11/2/2015.
 */
public class ShopProductListHeaderDelegate {

    public interface ProductHeaderListListener{
        void onToggleView();
        void onEtalaseClick(int pos);
        void onFilterClick(View v);
        void onSpinnerEtalaseClick();
    }

    private ProductHeaderListListener listener;
    private SimpleSpinnerAdapter etalaseAdapter;
    private int spinnerLastPos = 0;
    // if selection == -1, no selection request
    private int spinnerSelectedPos = -1;

    private class VHolder extends RecyclerView.ViewHolder{

        public ImageView toggle;
        public Spinner etalase;
        public View filterClick;

        public VHolder(View itemView) {
            super(itemView);
            toggle = (ImageView) itemView.findViewById(R.id.toggle_view);
            filterClick = itemView.findViewById(R.id.btn_filter_sort);
            etalase = (Spinner)itemView.findViewById(R.id.spinner_etalase);
        }
    }

    public void setEtalaseAdapter(SimpleSpinnerAdapter etalaseAdapter){
        this.etalaseAdapter = etalaseAdapter;
    }

    public void setSelectedEtalase(int pos){
        spinnerSelectedPos = pos;
    }

    public void setProductListHeader(ProductHeaderListListener listener){
        this.listener = listener;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_product_list_header, parent, false);
        return new VHolder(view);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int toggleIcon){
        VHolder vholder = (VHolder) holder;
        vholder.toggle.setOnClickListener(onToggleView());
        vholder.filterClick.setOnClickListener(onFilterClick());
        vholder.etalase.setOnItemSelectedListener(onEtalaseSelected());
        vholder.etalase.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    listener.onSpinnerEtalaseClick();
                }
                return false;
            }
        });
        if(vholder.etalase.getAdapter() == null)
            vholder.etalase.setAdapter(etalaseAdapter);
        vholder.toggle.setImageResource(toggleIcon);
        if(hasSelection(spinnerSelectedPos))
            vholder.etalase.setSelection(spinnerSelectedPos);
    }

    private boolean hasSelection(int spinnerSelectedPos) {
        return spinnerSelectedPos > -1;
    }

    private AdapterView.OnItemSelectedListener onEtalaseSelected() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != spinnerLastPos) {
                    spinnerLastPos = i;
                    listener.onEtalaseClick(i);
                }
                if(i == spinnerSelectedPos)
                    spinnerSelectedPos = -1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
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
