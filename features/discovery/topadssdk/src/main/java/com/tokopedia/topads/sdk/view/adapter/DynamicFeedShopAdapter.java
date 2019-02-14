package com.tokopedia.topads.sdk.view.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.gm.resource.GMConstant;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.ImageProduct;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.view.ImpressedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 07/01/19.
 */
public class DynamicFeedShopAdapter
        extends RecyclerView.Adapter<DynamicFeedShopAdapter.DynamicFeedShopViewHolder> {

    private final LocalAdsClickListener itemClickListener;

    private List<Data> list = new ArrayList<>();

    public DynamicFeedShopAdapter(LocalAdsClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public DynamicFeedShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_dynamic_feed_shop, parent, false);
        return new DynamicFeedShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicFeedShopViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Data> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull DynamicFeedShopViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }

    public class DynamicFeedShopViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView ivImageLeft, ivImageMiddle, ivProfile, ivBadge;
        private ImpressedImageView ivImageRight;
        private TextView tvDescription, tvName;
        private ButtonCompat btnFollow;
        private ImageLoader imageLoader;

        DynamicFeedShopViewHolder(View itemView) {
            super(itemView);
            this.imageLoader = new ImageLoader(itemView.getContext());

            this.itemView = itemView;
            this.ivImageLeft = itemView.findViewById(R.id.ivImageLeft);
            this.ivImageMiddle = itemView.findViewById(R.id.ivImageMiddle);
            this.ivImageRight = itemView.findViewById(R.id.ivImageRight);
            this.ivProfile = itemView.findViewById(R.id.ivProfile);
            this.ivBadge = itemView.findViewById(R.id.ivBadge);
            this.tvDescription = itemView.findViewById(R.id.tvDescription);
            this.tvName = itemView.findViewById(R.id.tvName);
            this.btnFollow = itemView.findViewById(R.id.btnFollow);
        }

        private void bind(Data data) {
            if (data == null) {
                return;
            }

            initView(data);
            initListener(data);
        }

        private void onViewRecycled() {
            ImageLoader.clearImage(ivImageLeft);
            ImageLoader.clearImage(ivImageMiddle);
            ImageLoader.clearImage(ivImageRight);
            ImageLoader.clearImage(ivProfile);
        }

        private void initView(Data data) {
            Shop shop = data.getShop();
            if (data.getShop() != null) {
                if (shop.getImageProduct() != null) {
                    List<ImageProduct> imageProductList = data.getShop().getImageProduct();
                    if (imageProductList.size() > 0) {
                        loadImageOrDefault(ivImageLeft, imageProductList.get(0).getImageUrl());
                    }
                    if (imageProductList.size() > 1) {
                        loadImageOrDefault(ivImageMiddle, imageProductList.get(1).getImageUrl());
                    }
                    if (imageProductList.size() > 2) {
                        ivImageRight.setImage(imageProductList.get(2));
                        ivImageRight.setViewHintListener(new ImpressedImageView.ViewHintListener() {
                            @Override
                            public void onViewHint() {
                                new ImpresionTask().execute(shop.getImageShop().getsUrl());
                            }
                        });
                    }
                    shop.setLoaded(true);
                }
                imageLoader.loadCircle(shop, ivProfile);
                tvName.setText(fromHtml(shop.getName()));
                tvDescription.setText(fromHtml(shop.getTagline()));
                bindBadge(shop);
            }

            bindFavorite(data);
        }

        private void initListener(Data data) {
            itemView.setOnClickListener(v ->
                    itemClickListener.onShopItemClicked(getAdapterPosition(), data)
            );
            btnFollow.setOnClickListener(v ->
                    itemClickListener.onAddFavorite(getAdapterPosition(), data)
            );
        }

        private void loadImageOrDefault(ImageView imageView, String imageUrl) {
            if (!TextUtils.isEmpty(imageUrl)) {
                imageLoader.loadImage(imageUrl, imageView);
            } else {
                imageView.setBackgroundColor(
                        ContextCompat.getColor(imageView.getContext(), R.color
                                .topads_gray_default_bg)
                );
            }
        }

        private Spanned fromHtml(String string) {
            if (string == null) {
                string = "";
            }

            Spanned result;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY);
            } else {
                result = Html.fromHtml(string);
            }
            return result;
        }

        private void bindFavorite(Data data) {
            if (data.isFavorit()) {
                btnFollow.setButtonCompatType(ButtonCompat.SECONDARY);
                btnFollow.setText(btnFollow.getContext().getString(R.string.topads_following));
            } else {
                btnFollow.setButtonCompatType(ButtonCompat.PRIMARY);
                btnFollow.setText(btnFollow.getContext().getString(R.string.topads_follow));
            }
        }

        private void bindBadge(Shop shop) {
            ViewGroup.MarginLayoutParams layoutParams
                    = (ViewGroup.MarginLayoutParams) tvName.getLayoutParams();

            if (shop.isGoldShopBadge()) {
                ivBadge.setVisibility(View.VISIBLE);
                ivBadge.setImageDrawable(
                        ImageLoader.getDrawable(
                                ivBadge.getContext(),
                                GMConstant.getGMDrawableResource(ivBadge.getContext())
                        )
                );
                layoutParams.leftMargin =
                        (int) ivBadge.getContext().getResources().getDimension(R.dimen.dp_4);

            } else if (shop.isShop_is_official()) {
                ivBadge.setVisibility(View.VISIBLE);
                ivBadge.setImageDrawable(
                        ImageLoader.getDrawable(ivBadge.getContext(), R.drawable.ic_official)
                );
                layoutParams.leftMargin =
                        (int) ivBadge.getContext().getResources().getDimension(R.dimen.dp_4);

            } else {
                ivBadge.setVisibility(View.GONE);
                layoutParams.leftMargin =
                        (int) ivBadge.getContext().getResources().getDimension(R.dimen.dp_0);
            }
        }
    }
}
