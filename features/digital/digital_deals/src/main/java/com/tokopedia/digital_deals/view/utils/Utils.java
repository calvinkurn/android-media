package com.tokopedia.digital_deals.view.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.model.DealsCategoryDomain;
import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;
import com.tokopedia.digital_deals.domain.model.PageDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel.DealsDetailsDomain;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel.MediaDomain;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel.Outlet;
import com.tokopedia.digital_deals.domain.model.locationdomainmodel.LocationItemDomain;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CatalogViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryViewModel;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;
import com.tokopedia.digital_deals.view.viewmodel.OutletViewModel;
import com.tokopedia.digital_deals.view.viewmodel.PageViewModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.tokopedia.digital_deals.view.utils.Utils.Constants.DIGITAL_DEALS;


public class Utils {
    private static Utils singleInstance;
    private static LocationViewModel location;
    public static String BRAND_QUERY_PARAM_TREE = "tree";
    public static String BRAND_QUERY_PARAM_BRAND = "brand";
    public static String BRAND_QUERY_PARAM_CHILD_CATEGORY_ID = "child_category_ids";
    public static String BRAND_QUERY_PARAM_CITY_ID = "cities";

    synchronized public static Utils getSingletonInstance() {
        if (singleInstance == null)
            singleInstance = new Utils();
        return singleInstance;
    }

    private Utils() {
        Log.d("UTILS", "Utils Instance created");
    }

    public ArrayList<CategoryViewModel> convertIntoCategoryListViewModel(List<DealsCategoryDomain> categoryList) {

        ArrayList<CategoryViewModel> categoryViewModels = new ArrayList<>();
        if (categoryList != null) {
            for (DealsCategoryDomain dealsCategoryDomain : categoryList) {

                CategoryViewModel category=new CategoryViewModel();
                category.setTitle(dealsCategoryDomain.getTitle());
                category.setCategoryId(dealsCategoryDomain.getId());
                category.setCount(dealsCategoryDomain.getCount());
                category.setName(dealsCategoryDomain.getName());
                category.setMediaUrl(dealsCategoryDomain.getMediaUrl());
                category.setUrl(dealsCategoryDomain.getUrl());
                category.setItems(convertIntoCategoryListItemsViewModel(dealsCategoryDomain.getItems()));

                switch (dealsCategoryDomain.getName().toLowerCase()) {
                    case "top":
                        categoryViewModels.add(0, category);
                        break;
                    case "carousel":
                        categoryViewModels.add(0, category);
                        break;
                    default:
                        categoryViewModels.add(category);
                        break;

                }

            }
        }
        return categoryViewModels;
    }

    public ArrayList<CategoryItemsViewModel> convertIntoCategoryListItemsViewModel(List<DealsCategoryItemDomain> categoryResponseItemsList) {
        ArrayList<CategoryItemsViewModel> categoryItemsViewModelList = new ArrayList<>();
        try {
            if (categoryResponseItemsList != null) {
                CategoryItemsViewModel categoryItemsViewModel;
                for (DealsCategoryItemDomain categoryEntity : categoryResponseItemsList) {
                    categoryItemsViewModel = new CategoryItemsViewModel();
                    categoryItemsViewModel.setMrp(categoryEntity.getMrp());
                    categoryItemsViewModel.setId(categoryEntity.getId());
                    categoryItemsViewModel.setDisplayName(categoryEntity.getDisplayName());
                    categoryItemsViewModel.setSalesPrice(categoryEntity.getSalesPrice());
                    categoryItemsViewModel.setSeoUrl(categoryEntity.getSeoUrl());
                    categoryItemsViewModel.setSoldQuantity(categoryEntity.getSoldQuantity());
                    categoryItemsViewModel.setImageWeb(categoryEntity.getImageWeb());
                    categoryItemsViewModel.setMrp(categoryEntity.getMrp());
                    categoryItemsViewModel.setThumbnailWeb(categoryEntity.getThumbnailWeb());
                    categoryItemsViewModel.setLikes(categoryEntity.getLikes());
                    categoryItemsViewModel.setSavingPercentage(categoryEntity.getSavingPercentage());

                    try {
                        BrandViewModel brandViewModel = new BrandViewModel();
                        brandViewModel.setTitle(categoryEntity.getBrand().getTitle());
                        brandViewModel.setFeaturedImage(categoryEntity.getBrand().getFeaturedImage());
                        brandViewModel.setFeaturedThumbnailImage(categoryEntity.getBrand().getFeaturedThumbnailImage());

                        categoryItemsViewModel.setBrand(brandViewModel);
                    } catch (Exception e) {

                    }

                    categoryItemsViewModel.setCityName(categoryEntity.getCityName());
                    categoryItemsViewModel.setMinStartDate(categoryEntity.getMinStartDate());
                    categoryItemsViewModel.setMaxEndDate(categoryEntity.getMaxEndDate());
                    categoryItemsViewModel.setLongRichDesc(categoryEntity.getLongRichDesc());
                    categoryItemsViewModel.setDisplayTags(categoryEntity.getDisplayTags());
//                categoryItemsViewModel.setDisplayTags(categoryEntity.getDisplayTags());
//                categoryItemsViewModel.setTnc(categoryEntity.getTnc());
//                categoryItemsViewModel.setIsTop(categoryEntity.getIsTop());

                    categoryItemsViewModel.setUrl(categoryEntity.getUrl());
                    categoryItemsViewModelList.add(categoryItemsViewModel);

                    CatalogViewModel catalogViewModel = new CatalogViewModel();
                    catalogViewModel.setDigitalCategoryId(categoryEntity.getCatalog().getDigitalCategoryId());
                    catalogViewModel.setDigitalProductId(categoryEntity.getCatalog().getDigitalProductId());
                    catalogViewModel.setDigitalProductCode(categoryEntity.getCatalog().getDigitalProductCode());
                    categoryItemsViewModel.setCatalog(catalogViewModel);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return categoryItemsViewModelList;
    }

    public List<LocationViewModel> convertIntoLocationListItemsViewModel(List<LocationItemDomain> locationItemsDomain) {
        List<LocationViewModel> locationItemsViewModelList = new ArrayList<>();
        if (locationItemsDomain != null) {
            LocationViewModel locationViewModel;
            for (LocationItemDomain locationItemDomain : locationItemsDomain) {
                locationViewModel = new LocationViewModel();
                locationViewModel.setId(locationItemDomain.getId());
                locationViewModel.setCountry(locationItemDomain.getCountry());
                locationViewModel.setDistrict(locationItemDomain.getDistrict());
                locationViewModel.setName(locationItemDomain.getName());
                locationViewModel.setSearchName(locationItemDomain.getSearchName());
                locationViewModel.setCategoryId(locationItemDomain.getCategoryId());
                locationViewModel.setUrl(locationItemDomain.getUrl());
                locationItemsViewModelList.add(locationViewModel);
            }
        }
        return locationItemsViewModelList;
    }


    public DealsDetailsViewModel convertIntoDealDetailsViewModel(DealsDetailsDomain detailsDomain) {
        DealsDetailsViewModel viewModel = new DealsDetailsViewModel();
        viewModel.setCategoryId(detailsDomain.getCategoryId());
        viewModel.setId(detailsDomain.getId());
        viewModel.setBrandId(detailsDomain.getBrandId());
        viewModel.setDisplayName(detailsDomain.getDisplayName());
        viewModel.setLongRichDesc(detailsDomain.getLongRichDesc());
        viewModel.setMrp(detailsDomain.getMrp());
        viewModel.setSalesPrice(detailsDomain.getSalesPrice());
        viewModel.setSavingPercentage(detailsDomain.getSavingPercentage());
        viewModel.setUrl(detailsDomain.getUrl());
        viewModel.setSaleEndDate(detailsDomain.getSaleEndDate());
        viewModel.setLikes(detailsDomain.getLikes());
        viewModel.setRecommendationUrl(detailsDomain.getRecommendationUrl());
        List<OutletViewModel> outletViewModel = null;
        if (detailsDomain.getOutlets() != null && detailsDomain.getOutlets().size() != 0) {

            outletViewModel = new ArrayList<>();
            for (Outlet outlet : detailsDomain.getOutlets()) {
                outletViewModel.add(convertIntoOutletViewModel(outlet));
            }
        }
        List<String> mediaUrl = null;
        if(detailsDomain.getMedia()!=null && detailsDomain.getMedia().size()!=0){
            mediaUrl=new ArrayList<>();
            for(MediaDomain mediaDomain: detailsDomain.getMedia())
                mediaUrl.add(mediaDomain.getUrl());
        }
        viewModel.setMediaUrl(mediaUrl);
        CatalogViewModel catalogViewModel = new CatalogViewModel();
        catalogViewModel.setDigitalCategoryId(detailsDomain.getCatalog().getDigitalCategoryId());
        catalogViewModel.setDigitalProductId(detailsDomain.getCatalog().getDigitalProductId());
        catalogViewModel.setDigitalProductCode(detailsDomain.getCatalog().getDigitalProductCode());
        viewModel.setCatalog(catalogViewModel);
        viewModel.setOutlets(outletViewModel);
        BrandViewModel brandViewModel = new BrandViewModel();
        brandViewModel.setTitle(detailsDomain.getBrand().getTitle());
        brandViewModel.setFeaturedImage(detailsDomain.getBrand().getFeaturedImage());
        brandViewModel.setFeaturedThumbnailImage(detailsDomain.getBrand().getFeaturedThumbnailImage());
        viewModel.setBrand(brandViewModel);
        viewModel.setImageWeb(detailsDomain.getImageWeb());
        viewModel.setThumbnailWeb(detailsDomain.getThumbnailWeb());

        return viewModel;
    }

    public OutletViewModel convertIntoOutletViewModel(Outlet outlet) {
        OutletViewModel viewModel = new OutletViewModel();
        viewModel.setProductId(outlet.getProductId());
        viewModel.setLocationId(outlet.getLocationId());
        viewModel.setName(outlet.getName());
        viewModel.setSearchName(outlet.getSearchName());
        viewModel.setDistrict(outlet.getDistrict());
        viewModel.setGmapAddress(outlet.getGmapAddress());
        viewModel.setNeighbourhood(outlet.getNeighbourhood());
        viewModel.setCoordinates(outlet.getCoordinates());
        viewModel.setState(outlet.getState());
        viewModel.setCountry(outlet.getCountry());
        return viewModel;
    }

    public List<BrandViewModel> convertIntoBrandListViewModel(List<BrandDomain> brandList) {

        List<BrandViewModel> brandViewModels = new ArrayList<>();
        if (brandList != null) {
            for (BrandDomain brandDomain : brandList) {
                brandViewModels.add(convertIntoBrandViewModel(brandDomain));
            }
        }
        return brandViewModels;
    }

    public BrandViewModel convertIntoBrandViewModel(BrandDomain brandDomain) {
        BrandViewModel brandViewModel = new BrandViewModel();
        brandViewModel.setTitle(brandDomain.getTitle());
        brandViewModel.setFeaturedImage(brandDomain.getFeaturedImage());
        brandViewModel.setDescription(brandDomain.getDescription());
        brandViewModel.setFeaturedThumbnailImage(brandDomain.getFeaturedThumbnailImage());
        brandViewModel.setUrl(brandDomain.getUrl());
        brandViewModel.setSeoUrl(brandDomain.getSeoUrl());
        return brandViewModel;
    }


    public PageViewModel convertIntoPageViewModel(PageDomain pageDomain) {

        PageViewModel pageViewModel = new PageViewModel();
        if (pageDomain != null) {
            pageViewModel.setUriNext(pageDomain.getUriNext());
            pageViewModel.setUriPrev(pageDomain.getUriPrev());
        }
        return pageViewModel;
    }



    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }

    private static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotNullOrEmpty(String string) {
        return !isNullOrEmpty(string);
    }


    public static String convertEpochToString(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", new Locale("in", "ID", ""));
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        return dateString;
    }

    public static Locale locale = new Locale("in", "ID");
    public static final String RUPIAH_FORMAT = "Rp %s";

    public static String convertToCurrencyString(Integer value) {
        return String.format(RUPIAH_FORMAT, NumberFormat.getNumberInstance(locale).format(value.longValue()));
    }

    public LocationViewModel getLocation(Context context) {

        if (location == null) {
            final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.DEALS_LOCATION);
            String locationjson = localCacheHandler.getString(TkpdCache.Key.KEY_DEALS_LOCATION, null);
            if (locationjson != null) {
                Gson gson = new Gson();
                location = gson.fromJson(locationjson, LocationViewModel.class);
            }
        }
        return location;
    }

    public void updateLocation(Context context, LocationViewModel locatn) {

        location = locatn;
        final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.DEALS_LOCATION);
        Gson gson = new Gson();
        String json = gson.toJson(location);
        localCacheHandler.putString(TkpdCache.Key.KEY_DEALS_LOCATION, json);
        localCacheHandler.applyEditor();
    }

    public void openGoogleMapsActivity(Context context, String latLng){
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+latLng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }else {
            Toast.makeText(context, context.getResources().getString(R.string.cannot_find_application), Toast.LENGTH_SHORT).show();
        }
    }

    public void setSnackBarLocationChange(String locationName, Context context, ViewGroup coordinatorLayout) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, locationName, Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        TextView textView = layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = LayoutInflater.from(context);
        View snackView = inflater.inflate(R.layout.custom_location_change_snackbar, null);
        TextView tv = snackView.findViewById(R.id.tv_location_ame);
        tv.setText(locationName.toUpperCase());
        TextView okbtn = snackView.findViewById(R.id.snack_ok);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        layout.addView(snackView, 0);
        layout.setPadding(0, 0, 0, 0);
        snackbar.show();
    }

    public void shareDeal(String deeplinkSlug, Context context, String name, String imageUrl){

        ShareData shareData = ShareData.Builder.aShareData()
                .setType("")
                .setName(name)
                .setUri(DIGITAL_DEALS+"/"+deeplinkSlug)
                .setImgUri(imageUrl)
                .build();
        BranchSdkUtils.generateBranchLink(shareData, (Activity) context,new BranchSdkUtils.GenerateShareContents() {
            @Override
            public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_TEXT, branchUrl);
                context.startActivity(Intent.createChooser(share, "Share link!"));
            }
        });
    }

    public static class Constants {

        public final static String DEALS = "deals";
        public static final String DIGITAL_DEALS = "tokopedia://digital-deals";
        public static final String DIGITAL_DEALS_DETAILS="tokopedia://digital-deals/{slug}";
    }

    public static String fetchOrderId(String url){
        return url.substring(url.lastIndexOf('/')+1);
    }

}
