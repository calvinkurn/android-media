package com.tokopedia.core.shopinfo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.app.TkpdCoreRouter;

/**
 * Created by Tkpd_Eka on 10/13/2015.
 */
public class ExtrasDelegate {

    private String shopId;

    public ExtrasDelegate() {
    }

    public ExtrasDelegate(String shopId) {
        this.shopId = shopId;
    }

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

    public RecyclerView.ViewHolder onCreateViewHolderNoResult(final ViewGroup parent){
        final Context context = parent.getContext().getApplicationContext();


        View view = null;
        if(shopId != null)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_result_shop_info, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_result, parent, false);

        ImageHandler.loadImageWithId(((ImageView)view.findViewById(R.id.no_result_image)), R.drawable.status_no_result);

        if(shopId != null && parent.getContext() != null && parent.getContext() instanceof Activity){
            boolean inMyShop = ((TkpdCoreRouter) context).isInMyShop(context, shopId);
            if(inMyShop){
                view.findViewById(R.id.button_add_product).setVisibility(View.VISIBLE);
                view.findViewById(R.id.button_add_product).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(parent.getContext() != null && parent.getContext() instanceof Activity){
                            ((TkpdCoreRouter)context).goToAddProduct(((Activity) parent.getContext()));
                        }
                    }
                });
            }else{
                view.findViewById(R.id.button_add_product).setVisibility(View.GONE);
            }

            view.findViewById(R.id.button_add_product).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(context != null && context instanceof TkpdCoreRouter){
                        if(parent.getContext() != null && parent.getContext() instanceof Activity){
                            ((TkpdCoreRouter)context).goToAddProduct(((Activity) parent.getContext()));
                        }
                    }
                }
            });
        }
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
