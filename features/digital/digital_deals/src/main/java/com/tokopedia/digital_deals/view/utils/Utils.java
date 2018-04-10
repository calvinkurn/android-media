package com.tokopedia.digital_deals.view.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.model.DealsCategoryDomain;
import com.tokopedia.digital_deals.domain.model.DealsItemDomain;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryViewModel;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;


public class Utils {
    private static Utils singleInstance;

    synchronized public static Utils getSingletonInstance() {
        if (singleInstance == null)
            singleInstance = new Utils();
        return singleInstance;
    }

    private Utils() {
        Log.d("UTILS", "Utils Instance created");
    }

    public List<CategoryViewModel> convertIntoCategoryListViewModel(List<DealsCategoryDomain> categoryList) {

        Log.d("MyListSize", " "+categoryList.size());
        List<CategoryViewModel> categoryViewModels = new ArrayList<>();
        if (categoryList != null) {
            for (DealsCategoryDomain eventsCategoryDomain : categoryList) {

                switch (eventsCategoryDomain.getName().toLowerCase()) {
                    case "top":
                        categoryViewModels.add(0, new CategoryViewModel(eventsCategoryDomain.getTitle(),
                                eventsCategoryDomain.getName(),
                                convertIntoCategoryListItemsViewModel(eventsCategoryDomain.getItems())));
                        break;
                    case "carousel":
                        categoryViewModels.add(0, new CategoryViewModel(eventsCategoryDomain.getTitle(),
                                eventsCategoryDomain.getName(),
                                convertIntoCategoryListItemsViewModel(eventsCategoryDomain.getItems())));
                        break;
                    default:
                        categoryViewModels.add(new CategoryViewModel(eventsCategoryDomain.getTitle(),
                                eventsCategoryDomain.getName(),
                                convertIntoCategoryListItemsViewModel(eventsCategoryDomain.getItems())));
                        break;

                }

            }
        }
        Log.d("MyListSize1234", " "+categoryViewModels.get(0).getName()+"  "+categoryViewModels.get(1).getName());
        Log.d("MyListSizeReturn", " "+categoryViewModels.size());
        return categoryViewModels;
    }

    public List<CategoryItemsViewModel> convertIntoCategoryListItemsViewModel(List<DealsItemDomain> categoryResponseItemsList) {
        List<CategoryItemsViewModel> categoryItemsViewModelList = new ArrayList<>();
        if (categoryResponseItemsList != null) {
            CategoryItemsViewModel categoryItemsViewModel;
            for (DealsItemDomain categoryEntity : categoryResponseItemsList) {
                categoryItemsViewModel = new CategoryItemsViewModel();
                categoryItemsViewModel.setMrp(categoryEntity.getMrp());
                categoryItemsViewModel.setDisplayName(categoryEntity.getDisplayName());
                categoryItemsViewModel.setSalesPrice(categoryEntity.getSalesPrice());
                categoryItemsViewModel.setSeoUrl(categoryEntity.getSeoUrl());
                categoryItemsViewModel.setSoldQuantity(categoryEntity.getSoldQuantity());
                categoryItemsViewModel.setImageWeb(categoryEntity.getImageWeb());
//                categoryItemsViewModel.setThumbnailApp(categoryEntity.getThumbnailApp());
//                categoryItemsViewModel.setMinStartTime(categoryEntity.getMinStartTime());
                categoryItemsViewModel.setCityName(categoryEntity.getCityName());
                categoryItemsViewModel.setMinStartDate(categoryEntity.getMinStartDate());
                categoryItemsViewModel.setMaxEndDate(categoryEntity.getMaxEndDate());
                categoryItemsViewModel.setLongRichDesc(categoryEntity.getLongRichDesc());
//                categoryItemsViewModel.setDisplayTags(categoryEntity.getDisplayTags());
//                categoryItemsViewModel.setTnc(categoryEntity.getTnc());
//                categoryItemsViewModel.setIsTop(categoryEntity.getIsTop());

                categoryItemsViewModel.setUrl(categoryEntity.getUrl());
                categoryItemsViewModelList.add(categoryItemsViewModel);
            }
        }
        return categoryItemsViewModelList;
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


    public static String convertEpochToString(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMM yyyy", new Locale("in", "ID", ""));
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        return dateString;
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
}
