package com.tokopedia.tkpd.home.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.tkpd.database.model.CategoryDB;
import com.tokopedia.tkpd.discovery.activity.BrowseProductActivity;
import com.tokopedia.tkpd.discovery.adapter.ProductAdapter;
import com.tokopedia.tkpd.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.tkpd.analytics.TrackingUtils;
import com.tokopedia.tkpd.var.RecyclerViewItem;
import com.tokopedia.tkpd.var.TkpdState;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by m.normansyah on 2/15/16.
 */
public class CategoryAdapter extends ProductAdapter{

    public CategoryAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch(getItemViewType(position)) {
            case TkpdState.RecyclerView.VIEW_CATEGORY:
                ((ViewHolder)viewHolder).onBind(position);
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
                break;
        }
    }

    @Override
    protected int isInType(RecyclerViewItem recyclerViewItem) {
        switch (recyclerViewItem.getType()){
            case TkpdState.RecyclerView.VIEW_CATEGORY:
                return recyclerViewItem.getType();
        }

        return super.isInType(recyclerViewItem);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case TkpdState.RecyclerView.VIEW_CATEGORY:
                View parentView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.gv_top_categories, viewGroup, false);
                ViewHolder viewHolder = new ViewHolder(parentView);
                return viewHolder;
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TkpdState.RecyclerView.VIEW_CATEGORY;
    }

    public class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder{

        @Bind(R.id.category_cardview)
        CardView categoryCardView;
        @Bind(R.id.image_cat)
        ImageView pImageView;
        @Bind(R.id.title_cat)
        TextView pNameView;
        Model data;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(int position){
             data = ((Model)getData().get(position));
            pImageView.setImageResource(data.drawRes);
            pNameView.setText(itemView.getContext().getString(data.textRes));
        }

        @OnClick(R.id.category_cardview)
        public void onClick(View view){
            BrowseProductActivity.moveTo(itemView.getContext(), data.depId + "", TopAdsApi.SRC_DIRECTORY);

            Map<String, String> values = new HashMap<String, String>();
            values.put(
                    itemView.getContext().getString(R.string.value_category_name),
                    itemView.getContext().getString(data.textRes)
            );

            TrackingUtils.eventLoca("event : "+
                    itemView.getContext().getString(R.string.event_click_category),values);

            CommonUtils.dumper("LocalTag : Event - CategoryApi : "
                    + itemView.getContext().getString(data.textRes));
        }
    }

    @Parcel
    public static class Model extends RecyclerViewItem{
        int drawRes = -1;
        int textRes = -1;
        int depId = -1;

        public Model(){
            this(-1,-1,-1);
        }

        public Model(String depId, int drawRes, int textRes){
            this(drawRes, textRes, Integer.parseInt(depId));
        }

        public Model(int drawRes, int textRes, int depId){
            setType(TkpdState.RecyclerView.VIEW_CATEGORY);
            this.drawRes = drawRes;
            this.textRes = textRes;
            this.depId = depId;
        }

        public static List<Model> mergeWithOnline(Context context, List<Model> offline, List<CategoryDB> online){
            for (int k=0;k<online.size();k++) {
                for (int m=0;m<offline.size();m++){
                    Model model = offline.get(m);
                    CategoryDB kategori = online.get(k);
                    if(context.getString(model.textRes).toLowerCase().equals(kategori.getNameCategory().toLowerCase())){
                        model.depId = kategori.getDepartmentId();
                        offline.remove(m);
                        offline.add(m, model);
                    }else{
                        Log.d("MNORMANSYAH", " lain "+kategori.getNameCategory()+" "+context.getString(model.textRes));
                    }
                }
            }
            return offline;
        }

        public static List<Model> getStaticList(){
            List<Model> list = new ArrayList<>();
            list.add(new Model("78", R.drawable.ic_cat_clothing_big, R.string.title_cat_clothing));
            list.add(new Model("65", R.drawable.ic_cat_phone_tablet_big, R.string.title_cat_phone));
            list.add(new Model("642", R.drawable.ic_cat_office_stationery_big, R.string.title_cat_office));
            list.add(new Model("79", R.drawable.ic_cat_fashion_big, R.string.title_cat_fashion));
            list.add(new Model("288", R.drawable.ic_cat_notebook_big, R.string.title_cat_notebook));
            list.add(new Model("54", R.drawable.ic_cat_gifts_big, R.string.title_cat_gift));
            list.add(new Model("61", R.drawable.ic_cat_beauty_big, R.string.title_cat_beauty));
            list.add(new Model("297", R.drawable.ic_cat_pc_big, R.string.title_cat_pc));
            list.add(new Model("55", R.drawable.ic_cat_toy_hobbies_big, R.string.title_cat_toys));
            list.add(new Model("715", R.drawable.ic_cat_health_big, R.string.title_cat_health));
            list.add(new Model("60", R.drawable.ic_cat_electronic_big, R.string.title_cat_elektronik));
            list.add(new Model("35", R.drawable.ic_cat_food_beverages_big, R.string.title_cat_foodbev));
            list.add(new Model("984", R.drawable.ic_cat_home_appliances_big, R.string.title_cat_home_appliances));
            list.add(new Model("578", R.drawable.ic_cat_photography_big, R.string.title_cat_photography));
            list.add(new Model("8", R.drawable.ic_cat_books_big, R.string.title_cat_books));
            list.add(new Model("983", R.drawable.ic_cat_kitchen_dining_big, R.string.title_cat_kitchen));
            list.add(new Model("63", R.drawable.ic_cat_automotive_big, R.string.title_cat_automotive));
            list.add(new Model("20", R.drawable.ic_cat_softwares_big, R.string.title_cat_software));
            list.add(new Model("56", R.drawable.ic_cat_baby_big, R.string.title_cat_baby));
            list.add(new Model("62", R.drawable.ic_cat_sports_big, R.string.title_cat_sport));
            list.add(new Model("57", R.drawable.ic_cat_movies_games_music_big, R.string.title_cat_movie));
            list.add(new Model("36", R.drawable.ic_cat_everything_else_big, R.string.title_everything_else));
            return list;
        }
    }
}
