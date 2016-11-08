package com.tokopedia.core.shopinfo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;

/**
 * Created by Tkpd_Eka on 10/13/2015.
 */
public class ExtrasDelegate {

    public class RetryHolder extends RecyclerView.ViewHolder{

        View button;

        public RetryHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.main_retry);
        }
    }

    public class EmptyStateHolder extends RecyclerView.ViewHolder{

        View retryButon;
        TextView msgRetry;

        public EmptyStateHolder(View itemView){
            super(itemView);
            retryButon = itemView.findViewById(R.id.button_retry);
            msgRetry = (TextView) itemView.findViewById(R.id.message_retry);
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolderLoading(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_layout, parent, false);
        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    public RecyclerView.ViewHolder onCreateViewHolderNoResult(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_result, parent, false);
        ImageHandler.loadImageWithId(((ImageView)view.findViewById(R.id.no_result_image)), R.drawable.status_no_result);
        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    public RecyclerView.ViewHolder onCreateViewHolderRetry(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_retry_footer, parent, false);
        return new RetryHolder(view);
    }

    public void onBindRetry(RecyclerView.ViewHolder holder, View.OnClickListener listener){
        ((RetryHolder)holder).button.setOnClickListener(listener);
    }

    public RecyclerView.ViewHolder onCreateViewHolderEmptyState(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_error_network, parent, false);
        return new EmptyStateHolder(view);
    }

    public void onBindRetryEmpty(RecyclerView.ViewHolder holder, final ShopProductListAdapter.RetryClickedListener listener, String message){
        ((EmptyStateHolder)holder).retryButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRetryClicked();
            }
        });
        ((EmptyStateHolder)holder).msgRetry.setText(message);
    }

}
