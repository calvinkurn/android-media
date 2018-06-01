package com.tokopedia.digital_deals.view.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.model.PageDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;
import com.tokopedia.digital_deals.domain.model.DealsCategoryDomain;
import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel.DealsDetailsDomain;
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
import com.tokopedia.digital_deals.view.viewmodel.SearchViewModel;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;


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

                switch (dealsCategoryDomain.getName().toLowerCase()) {
                    case "top":
                        categoryViewModels.add(0, new CategoryViewModel(dealsCategoryDomain.getTitle(),
                                dealsCategoryDomain.getName(), dealsCategoryDomain.getUrl(), dealsCategoryDomain.getMediaUrl(),
                                convertIntoCategoryListItemsViewModel(dealsCategoryDomain.getItems())));
                        break;
                    case "carousel":
                        categoryViewModels.add(0, new CategoryViewModel(dealsCategoryDomain.getTitle(),
                                dealsCategoryDomain.getName(), dealsCategoryDomain.getUrl(), dealsCategoryDomain.getMediaUrl(),
                                convertIntoCategoryListItemsViewModel(dealsCategoryDomain.getItems())));
                        break;
                    default:
                        categoryViewModels.add(new CategoryViewModel(dealsCategoryDomain.getTitle(),
                                dealsCategoryDomain.getName(), dealsCategoryDomain.getUrl(), dealsCategoryDomain.getMediaUrl(),
                                convertIntoCategoryListItemsViewModel(dealsCategoryDomain.getItems())));
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
//                categoryItemsViewModel.setThumbnailApp(categoryEntity.getThumbnailApp());
//                categoryItemsViewModel.setMinStartTime(categoryEntity.getMinStartTime());
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

    public ArrayList<SearchViewModel> convertIntoSearchViewModel(List<CategoryViewModel> source) {
        ArrayList<SearchViewModel> searchViewModels = new ArrayList<>();
        if (source != null) {
            SearchViewModel searchModelItem;
            for (CategoryViewModel item : source) {
                if (item.getItems() != null) {
                    List<CategoryItemsViewModel> sourceModels = item.getItems();
                    for (CategoryItemsViewModel sourceItem : sourceModels) {
//                        if (sourceItem.getIsTop() == 1 && !isPresent(searchViewModels, sourceItem.getTitle())) {
                        searchModelItem = new SearchViewModel();
                        searchModelItem.setCityName(sourceItem.getCityName());
                        searchModelItem.setDisplayName(sourceItem.getDisplayName());
//                            searchModelItem.setImageApp(sourceItem.getImageApp());
                        searchModelItem.setMaxEndDate(sourceItem.getMaxEndDate());
                        searchModelItem.setMinStartDate(sourceItem.getMinStartDate());
                        searchModelItem.setSalesPrice(sourceItem.getSalesPrice());
                        searchModelItem.setTitle(sourceItem.getDisplayName());
                        searchModelItem.setUrl(sourceItem.getUrl());
                        searchViewModels.add(searchModelItem);
//                        }
                    }
                }

            }
        }
        return searchViewModels;
    }

    public PageViewModel convertIntoPageViewModel(PageDomain pageDomain) {

        PageViewModel pageViewModel = new PageViewModel();
        if (pageDomain != null) {
            pageViewModel.setUriNext(pageDomain.getUriNext());
            pageViewModel.setUriPrev(pageDomain.getUriPrev());
        }
        return pageViewModel;
    }

    private boolean isPresent(ArrayList<SearchViewModel> searchViewModels, String title) {
        for (SearchViewModel viewModel : searchViewModels) {
            if (viewModel.getTitle().equals(title))
                return true;
        }
        return false;
    }

    public List<SearchViewModel> convertSearchResultsToModel(List<CategoryItemsViewModel> categoryItemsViewModels) {
        List<SearchViewModel> searchResults = null;
        if (categoryItemsViewModels != null && !categoryItemsViewModels.isEmpty()) {
            SearchViewModel searchModelItem;
            searchResults = new ArrayList<>();
            for (CategoryItemsViewModel sourceItem : categoryItemsViewModels) {
                searchModelItem = new SearchViewModel();
                searchModelItem.setCityName(sourceItem.getCityName());
                searchModelItem.setDisplayName(sourceItem.getDisplayName());
//                searchModelItem.setImageApp(sourceItem.getImageApp());
                searchModelItem.setMaxEndDate(sourceItem.getMaxEndDate());
                searchModelItem.setMinStartDate(sourceItem.getMinStartDate());
                searchModelItem.setSalesPrice(sourceItem.getSalesPrice());
                searchModelItem.setTitle(sourceItem.getDisplayName());
                searchModelItem.setUrl(sourceItem.getUrl());
                searchResults.add(searchModelItem);
            }
        }
        return searchResults;
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

    public static List<String> getDisplayTags(String displayTag) {
        List<String> displayTagsList = new ArrayList<>();

        String[] temp = displayTag.split("\\|");
        for (int i = 0; i < temp.length; i++) {
            displayTagsList.add(temp[i]);
        }

        return displayTagsList;
    }


    public static String[] getDateArray(String dateRange) {
        String[] date = new String[3];
        date[0] = dateRange.substring(0, 3);//day
        //Sat, 14 Apr 2018 - Sat, 14 Apr 2018
        date[1] = dateRange.substring(5, 7).trim();//date
        date[2] = dateRange.substring(7, 11).trim();//month
        return date;
    }

    private static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotNullOrEmpty(String string) {
        return !isNullOrEmpty(string);
    }

    public static Bitmap getBitmap(Context context, LinearLayout v) {
        v.setDrawingCacheEnabled(true);
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Bitmap bmp = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        canvas.drawColor(ContextCompat.getColor(context, R.color.preview_bg));
        v.draw(canvas);
        return bmp;
    }

    public static void saveImage(Context context, Bitmap finalBitmap) {


        String extStorageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString();
        File folder = new File(extStorageDirectory, "store_image");
        if (!folder.exists())
            folder.mkdir();

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image" + n + ".png";

        File pdfFile = new File(folder, fname);

        try {
            pdfFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(pdfFile);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public void setSnackBarLocationChange(String locationName, Context context, CoordinatorLayout coordinatorLayout) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, locationName, Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        //layout.setBackgroundColor(getResources().getColor(R.color.red_100));
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
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

    public static class Constants {

        public final static String DEALS = "deals";
    }

    public static String fetchOrderId(String url){
        return url.substring(url.lastIndexOf('/')+1);
    }

}
