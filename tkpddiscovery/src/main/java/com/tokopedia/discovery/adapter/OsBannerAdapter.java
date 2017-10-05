package com.tokopedia.discovery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.entity.discovery.BannerOfficialStoreModel;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brilliant.oka on 02/06/17.
 */

public class OsBannerAdapter {
    public static final String KEYWORD = "keyword";
    public static final String ETALASE_NAME = "etalase_name";

    /**
     * Create new Official Stores Banner View Holder
     *
     * @param parent parent view
     * @return view holder of Official Store Banner
     */
    public static BannerOsViewHolder onCreateBannerOfficialStore(Context context, ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.official_store_banner, parent, false);
        return new BannerOsViewHolder(context, inflate);
    }

    /**
     * View Model for Official Stores Banner
     */
    public static class OsBannerViewModel extends RecyclerViewItem {
        private BannerOfficialStoreModel bannerOfficialStore;

        private OsBannerViewModel() {
            setType(TkpdState.RecyclerView.VIEW_BANNER_OFFICIAL_STORE);
        }

        public OsBannerViewModel(BannerOfficialStoreModel bannerOfficialStore) {
            this();
            this.bannerOfficialStore = bannerOfficialStore;
        }
    }

    /**
     * View Holder for Official Store Banner
     */
    public static class BannerOsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.image_banner_os)
        ImageView imageBannerOs;

        Context context;

        public BannerOsViewHolder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;
        }

        public void bind(final OsBannerViewModel viewModel) {
            ImageHandler.loadImageWithTarget(context, viewModel.bannerOfficialStore.getBannerUrl(),
                    new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (resource.getHeight() != 1 && resource.getWidth() != 1) {
                                imageBannerOs.setImageBitmap(resource);
                            }
                        }
                    }
            );

            imageBannerOs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToUrl(viewModel.bannerOfficialStore.getShopUrl());

                    // GTM Tracker
                    UnifyTracking.eventClickOsBanner(
                            viewModel.bannerOfficialStore.getBannerUrl()
                                    + " - "
                                    + viewModel.bannerOfficialStore.getKeyword()
                    );
                }
            });
        }

        private void goToUrl(String url) {
            switch ((DeepLinkChecker.getDeepLinkType(url))) {
                case DeepLinkChecker.BROWSE:
                    DeepLinkChecker.openBrowse(url, context);
                    break;
                case DeepLinkChecker.HOT:
                    DeepLinkChecker.openHot(url, context);
                    break;
                case DeepLinkChecker.HOT_LIST:
                    DeepLinkChecker.openHomepage(context,  HomeRouter.INIT_STATE_FRAGMENT_HOTLIST);
                    break;
                case DeepLinkChecker.CATALOG:
                    DeepLinkChecker.openCatalog(url, context);
                    break;
                case DeepLinkChecker.PRODUCT:
                    DeepLinkChecker.openProduct(url, context);
                    break;
                case DeepLinkChecker.SHOP:
                    Bundle bundle = new Bundle();
                    if (DeepLinkChecker.getQuery(url, KEYWORD) != null) {
                        bundle.putString(KEYWORD, DeepLinkChecker.getQuery(url, KEYWORD));
                    }
                    DeepLinkChecker.openShopWithParameter(url, context, bundle);
                    break;
                case DeepLinkChecker.ETALASE:
                    bundle = new Bundle();
                    bundle.putString(ETALASE_NAME, DeepLinkChecker.getLinkSegment(url).get(2));
                    if (DeepLinkChecker.getQuery(url, KEYWORD) != null) {
                        bundle.putString(KEYWORD, DeepLinkChecker.getQuery(url, KEYWORD));
                    }
                    DeepLinkChecker.openShopWithParameter(url, context, bundle);
                    break;
                default:
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
