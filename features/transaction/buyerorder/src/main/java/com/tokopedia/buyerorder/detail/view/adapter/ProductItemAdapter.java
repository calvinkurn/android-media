package com.tokopedia.buyerorder.detail.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalOrder;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.detail.data.ActionButton;
import com.tokopedia.buyerorder.detail.data.Items;
import com.tokopedia.buyerorder.detail.data.Status;
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics;
import com.tokopedia.buyerorder.detail.view.fragment.MarketPlaceDetailFragment;
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailPresenter;

import java.util.ArrayList;
import java.util.List;

public class ProductItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Items> itemsList;
    private Context context;
    private Status status;
    private String userId;
    private String orderId;
    private OrderListDetailPresenter presenter;
    private boolean isOrderTradeIn;
    public static final String ORDER_LIST_URL_ENCODING = "UTF-8";
    public OrderListAnalytics orderListAnalytics;
    private static final String BUY_AGAIN_ACTION_BUTTON_KEY = "buy_again";
    private static final String CLICK_SIMILAR_PRODUCT_LEVEL_PRODUCT = "click lihat produk serupa - product";


    public ProductItemAdapter(Context context, List<Items> itemsList, OrderListDetailPresenter presenter,
                              boolean isTradeIn, Status status, String userId, String orderId) {
        this.context = context;
        this.itemsList = itemsList;
        this.presenter = presenter;
        this.isOrderTradeIn = isTradeIn;
        this.status = status;
        this.userId = userId;
        this.orderId = orderId;
        orderListAnalytics = new OrderListAnalytics();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.marketplace_product_detail_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ProductItemAdapter.ItemViewHolder) holder).setIndex(position);
        ((ProductItemAdapter.ItemViewHolder) holder).bindData(itemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int index;
        private ImageView productImage;
        private TextView productName;
        private TextView quantity;
        private TextView productPrice;
        private TextView productDescription, totalPrice, buyBtn;
        private TextView labelTradeIn;
        private ImageView freeShippingImage;


        ItemViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.img_product);
            productName = itemView.findViewById(R.id.txt_product_name);
            quantity = itemView.findViewById(R.id.txt_quantity);
            productPrice = itemView.findViewById(R.id.txt_item_price);
            freeShippingImage = itemView.findViewById(R.id.image_bebas_ongkir);
            productDescription = itemView.findViewById(R.id.txt_description);
            totalPrice = itemView.findViewById(R.id.txt_total_price);
            buyBtn = itemView.findViewById(R.id.btn_buy);
            labelTradeIn = itemView.findViewById(R.id.label_trade_in);
        }

        void bindData(final Items items) {
            if (isOrderTradeIn) {
                labelTradeIn.setVisibility(View.VISIBLE);
            } else {
                if (labelTradeIn.getVisibility() != View.GONE)
                    labelTradeIn.setVisibility(View.GONE);
            }
            if (items != null && items.getFreeShipping() != null && items.getFreeShipping().isEligible()) {
                freeShippingImage.setVisibility(View.VISIBLE);
                ImageHandler.loadImage(context, freeShippingImage, items.getFreeShipping().getImageUrl(), com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);
            }
            if (items != null) {
                if (!TextUtils.isEmpty(items.getImageUrl())) {
                    ImageHandler.loadImage(context, productImage, items.getImageUrl(), com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);
                }
                if (!TextUtils.isEmpty(items.getTitle())) {
                    productName.setText(Html.fromHtml(items.getTitle()));
                }
                quantity.setText(String.format(context.getResources().getString(R.string.quantity), items.getQuantity(), items.getWeight()));
                if (!TextUtils.isEmpty(items.getPrice()))
                    productPrice.setText(items.getPrice());

                if (!TextUtils.isEmpty(items.getDescription())) {
                    productDescription.setText(MethodChecker.fromHtml(items.getDescription()));
                }else {
                    productDescription.setVisibility(View.GONE);
                }
            }

            if (!TextUtils.isEmpty(items.getTotalPrice())) {
                totalPrice.setText(items.getTotalPrice());
            }
            if (items.getActionButtons().size() > 0) {
                ActionButton actionButton = items.getActionButtons().get(0);
                buyBtn.setVisibility(View.VISIBLE);
                buyBtn.setText(actionButton.getLabel());
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadius(context.getResources().getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_4));
                if (!actionButton.getActionColor().getBackground().equals("")) {
                    shape.setColor((Color.parseColor(actionButton.getActionColor().getBackground())));
                }
                if (!actionButton.getActionColor().getBorder().equals("")) {
                    shape.setStroke(context.getResources().getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_2), Color.parseColor(actionButton.getActionColor().getBorder()));
                }
                buyBtn.setBackground(shape);
                if (!actionButton.getActionColor().getTextColor().equals("")) {
                    buyBtn.setTextColor(Color.parseColor(actionButton.getActionColor().getTextColor()));
                }
                buyBtn.setOnClickListener(view -> {
                    if (!TextUtils.isEmpty(actionButton.getUri())) {
                        if (actionButton.getKey().equalsIgnoreCase(MarketPlaceDetailFragment.SIMILAR_PRODUCTS_ACTION_BUTTON_KEY)) {
                            orderListAnalytics.sendActionButtonClickEvent(CLICK_SIMILAR_PRODUCT_LEVEL_PRODUCT, items.getId());
                        }
                        RouteManager.route(context, actionButton.getUri());
                    } else if (actionButton.getKey().equalsIgnoreCase(BUY_AGAIN_ACTION_BUTTON_KEY)) {
                        List<Items> itemsList = new ArrayList<>();
                        itemsList.add(items);
                        presenter.onBuyAgainItems(GraphqlHelper.loadRawString(context.getResources(), com.tokopedia.atc_common.R.raw.mutation_add_to_cart_multi), itemsList, " - product", status.status());
                    }
                });
            }

            itemView.setOnClickListener(view -> {
                orderListAnalytics.sendProductClickDetailsEvent(items, getIndex(), status.status());

                String applinkSnapshot = ApplinkConst.SNAPSHOT_ORDER+"/"+ orderId +"/"+ items.getOrderDetailId();
                RouteManager.route(context, applinkSnapshot);
            });

        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public void onClick(View view) {

        }
    }
}