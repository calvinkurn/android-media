package com.tokopedia.core.shopinfo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.ShopLocation;
import com.tokopedia.core.ShopStatisticDetail;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.app.V2BaseFragment;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.shopinfo.activity.ShopFavoritedActivity;
import com.tokopedia.core.shopinfo.models.shopmodel.ShipmentPackage;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tkpd_Eka on 11/5/2015.
 */
public class FragmentShopStatistic extends V2BaseFragment {

    private class ViewHolder {
        private TextView ShopName;
        private TextView DetailShopName;
        private TextView ShopTag;
        private TextView ShopDesc;
        private TextView LastLogin;
        private TextView SoldItems;
        private TextView SoldTitle;
        private TextView ProductSoldTitle;
        private TextView Favorited;
        private TextView ShopMainLocation;
        private TextView OpenSince;
        private ImageView ShopAvatar;
        private ImageView Speed;
        private String ShopAddrParam;
        private String ShopId;
        private String OwnerId;
        private TextView OwnerName;
        private TextView OwnerMail;
        private TextView ShopAddress;
        private TextView ShopPhone;
        private TextView ShopFax;
        private TextView ShopEmail;
        private TextView SuccessfulTx;
        private TextView SuccessfulTxTitle;
        private TextView ProdSold;
        private TextView TtlEtalase;
        private TextView TtlProd;
        private TextView reputationPoint;
        private TextView speedText;
        private ImageView OwnerPicture;
        private LinearLayout AddressLayout;
        private LinearLayout EmailField;
        private TextView SeeAllAddr;
        private String OwnerPictureUri;
        private boolean IsOwner;
        private String ShopAddressList;
        private LinearLayout ShippingAgencyListView;
        private LinearLayout medal;
        private View openDetailStatistic;
    }

    public static final int ICON_FAST = R.drawable.ic_icon_repsis_speed_cepat;
    public static final int ICON_MEDIUM = R.drawable.ic_icon_repsis_speed_sedang;
    public static final int ICON_SLOW = R.drawable.ic_icon_repsis_speed_lambat;

    public static final String BADGE_FAST = "badge-speed-good";
    public static final String BADGE_MEDIUM = "badge-speed-neutral";
    public static final String BADGE_SLOW = "badge-speed-bad";

    private ArrayList<Integer> StarIconLocation = new ArrayList<Integer>();

    private ShopModel model;
    private ViewHolder holder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String data = getActivity().getIntent().getStringExtra("shop_info");
        model = new Gson().fromJson(data, ShopModel.class);
        StarIconLocation.add(R.drawable.ic_star_none);
        StarIconLocation.add(R.drawable.ic_star_one);
        StarIconLocation.add(R.drawable.ic_star_two);
        StarIconLocation.add(R.drawable.ic_star_three);
        StarIconLocation.add(R.drawable.ic_star_four);
        StarIconLocation.add(R.drawable.ic_star_five);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_shop_statistic;
    }

    @Override
    protected void onCreateView() {
        bindViewHolder();
    }

    @Override
    protected Object getHolder() {
        return holder;
    }

    @Override
    protected void setHolder(Object holder) {
        this.holder = (ViewHolder) holder;
    }

    @Override
    protected void initView() {
        holder = new ViewHolder();
        holder.ShopAvatar = (ImageView) findViewById(R.id.shop_avatar);
        holder.AddressLayout = (LinearLayout) findViewById(R.id.address_layout);
        holder.ShopName = (TextView) findViewById(R.id.shop_name);
        holder.ShopTag = (TextView) findViewById(R.id.shop_tag_line);
        holder.ShopDesc = (TextView) findViewById(R.id.short_desc);
        holder.LastLogin = (TextView) findViewById(R.id.last_login);
        holder.SoldItems = (TextView) findViewById(R.id.sold_items);
        holder.SoldTitle = (TextView) findViewById(R.id.title_sold);
        holder.ProductSoldTitle = (TextView) findViewById(R.id.product_sold_title);
        holder.EmailField = (LinearLayout) findViewById(R.id.email_field);
        holder.Favorited = (TextView) findViewById(R.id.favorited);
        holder.ShopMainLocation = (TextView) findViewById(R.id.shop_location);
        holder.OpenSince = (TextView) findViewById(R.id.open_since);
        holder.Speed = (ImageView) findViewById(R.id.speed);
        holder.OwnerName = (TextView) findViewById(R.id.owner_name);
        holder.OwnerMail = (TextView) findViewById(R.id.owner_email);
        holder.DetailShopName = (TextView) findViewById(R.id.detail_shop_name);
        holder.ShopAddress = (TextView) findViewById(R.id.detail_shop_address);
        holder.ShopPhone = (TextView) findViewById(R.id.detail_shop_phone);
        holder.ShopFax = (TextView) findViewById(R.id.detail_shop_fax);
        holder.ShopEmail = (TextView) findViewById(R.id.detail_shop_email);
        holder.SuccessfulTx = (TextView) findViewById(R.id.successfull_transaction);
        holder.SuccessfulTxTitle = (TextView) findViewById(R.id.success_transaction_title);
        holder.ProdSold = (TextView) findViewById(R.id.product_sold);
        holder.TtlEtalase = (TextView) findViewById(R.id.total_etalase);
        holder.TtlProd = (TextView) findViewById(R.id.total_product);
        holder.speedText = (TextView) findViewById(R.id.transaction_speed);
        holder.OwnerPicture = (ImageView) findViewById(R.id.owner_picture);
        holder.SeeAllAddr = (TextView) findViewById(R.id.shop_detail_all_addr);
        holder.ShippingAgencyListView = (LinearLayout) findViewById(R.id.shipping_agency_listtview);
        holder.medal = (LinearLayout) findViewById(R.id.reputation_medal);
        holder.openDetailStatistic = findViewById(R.id.lihat_detail);
        holder.reputationPoint = (TextView) findViewById(R.id.reputation_point);
    }

    private void bindViewHolder() {
        ImageHandler.loadImageRounded2(getActivity(), holder.ShopAvatar, model.info.shopAvatar);
//        ImageHandler.LoadImageRounded(holder.ShopAvatar, model.info.shopAvatar);
        ImageHandler.loadImageRounded2(getActivity(), holder.OwnerPicture, model.owner.ownerImage);
//        ImageHandler.LoadImageRounded(holder.OwnerPicture, model.owner.ownerImage);

        holder.ShopName.setText(MethodChecker.fromHtml(model.info.shopName).toString());
        holder.ShopTag.setText(MethodChecker.fromHtml(model.info.shopTagline).toString());
        holder.ShopDesc.setText(MethodChecker.fromHtml(model.info.shopDescription).toString());
        holder.LastLogin.setText(model.info.shopOwnerLastLogin);
        holder.SoldItems.setText(model.stats.shopItemSold);
        holder.Favorited.setText(model.info.shopTotalFavorit + "");
        holder.ShopMainLocation.setText(model.info.shopLocation);
        holder.OpenSince.setText(model.info.shopOpenSince);
        holder.OwnerName.setText(model.owner.ownerName);
        holder.OwnerMail.setText(model.owner.ownerEmail);
        holder.SuccessfulTx.setText(model.stats.txCountSuccess);
        holder.ProdSold.setText(model.stats.shopItemSold);
        holder.TtlEtalase.setText(model.stats.shopTotalEtalase);
        holder.TtlProd.setText(model.stats.shopTotalProduct);
        holder.reputationPoint.setText(model.stats.shopReputationScore + " Poin");
        if (model.address.size() > 0) {
            holder.DetailShopName.setText(MethodChecker.fromHtml(model.address.get(0).locationAddressName).toString());
            holder.ShopAddress.setText(MethodChecker.fromHtml(model.address.get(0).locationAddress).toString() + "\n" + MethodChecker.fromHtml(model.address.get(0).locationArea).toString());
            holder.ShopEmail.setText(model.address.get(0).locationEmail);
            holder.ShopPhone.setText(model.address.get(0).locationPhone);
            holder.ShopFax.setText(model.address.get(0).locationFax);
            checkIfEmpty(holder.ShopEmail, model.address.get(0).locationEmail);
            checkIfEmpty(holder.ShopFax, model.address.get(0).locationFax);
            checkIfEmpty(holder.ShopPhone, model.address.get(0).locationPhone);
        }
        else
            holder.AddressLayout.setVisibility(View.GONE);

        checkIfEmpty(holder.EmailField, model.owner.ownerEmail);

        if (model.address.size() < 2)
            holder.SeeAllAddr.setVisibility(View.GONE);

        addSupportedShipping();
        ReputationLevelUtils.setReputationMedals(getActivity(), holder.medal, model.stats.shopBadgeLevel.set, model.stats.shopBadgeLevel.level, model.stats.shopReputationScore);
        if(model.info.shopIsOfficial==1){
            holder.SoldTitle.setVisibility(View.GONE);
            holder.SoldItems.setVisibility(View.GONE);
            holder.SuccessfulTxTitle.setVisibility(View.GONE);
            holder.SuccessfulTx.setVisibility(View.GONE);
            holder.ProdSold.setVisibility(View.GONE);
            holder.ProductSoldTitle.setVisibility(View.GONE);
        }
    }

    private void checkIfEmpty(View view, String info) {
        if (info.equals("0"))
            view.setVisibility(View.GONE);
    }

    private void addSupportedShipping() {
        int total = model.shipment.size();
        for (int i = 0; i < total; i++) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.listview_shipment_support, null);
            ImageView imv = (ImageView) v.findViewById(R.id.agency_logo);
            imv.getLayoutParams().height = (int) getActivity().getResources().getDimension(R.dimen.img_thumb);
            imv.getLayoutParams().width = (int) getActivity().getResources().getDimension(R.dimen.img_thumb);
            TextView package_list = (TextView) v.findViewById(R.id.package_list);
            package_list.setText(getShipmentPackages(model.shipment.get(i).shipmentPackage));
            ImageHandler.LoadImage(imv, model.shipment.get(i).shipmentImage);
            holder.ShippingAgencyListView.addView(v);
        }
    }

    private String getShipmentPackages(List<ShipmentPackage> pack) {
        if(pack==null || pack.isEmpty())
            return "";
        String packages = pack.get(0).productName;
        int total = pack.size();
        for(int i = 1; i< total; i++){
            packages = packages + System.getProperty("line.separator")  + pack.get(i).productName;
        }
        return packages;
    }

    @Override
    protected void setListener() {
        holder.openDetailStatistic.setOnClickListener(openDetailStatistic());
        holder.SeeAllAddr.setOnClickListener(onSeeAllAddrClick());
        holder.OwnerName.setOnClickListener(onOwnerClick());
        holder.OwnerPicture.setOnClickListener(onOwnerClick());
        holder.Favorited.setOnClickListener(onFavoritedClick());
    }

    private View.OnClickListener onFavoritedClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopFavoritedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("shop_id", model.info.shopId);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        };
    }

    private View.OnClickListener onOwnerClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(((TkpdCoreRouter) getActivity().getApplicationContext())
                        .getTopProfileIntent(getActivity(), String.valueOf(model.owner.ownerId)));
            }
        };
    }

    private View.OnClickListener onSeeAllAddrClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAllAddress();
            }
        };
    }

    private void openAllAddress() {
        Intent intent = new Intent(getActivity(), ShopLocation.class);
        intent.putExtra("is_owner", model.info.shopIsOwner != 0);
        intent.putExtra("shop_info", getActivity().getIntent().getStringExtra("shop_info"));
        getActivity().startActivity(intent);
    }

    private View.OnClickListener onDetailClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetailStatistic();
            }
        };
    }

    private View.OnClickListener openDetailStatistic() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopStatisticDetail.class);
                intent.putExtra(ShopStatisticDetail.EXTRA_SHOP_INFO, getActivity().getIntent().getStringExtra("shop_info"));
                getActivity().startActivity(intent);
            }
        };
    }

}
