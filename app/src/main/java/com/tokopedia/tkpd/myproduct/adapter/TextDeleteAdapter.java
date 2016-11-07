package com.tokopedia.tkpd.myproduct.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.myproduct.ProductActivity;
import com.tokopedia.tkpd.myproduct.model.SimpleTextModel;
import com.tokopedia.tkpd.myproduct.model.TextDeleteModel;

import java.util.ArrayList;
import java.util.List;

import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by m.normansyah on 08/12/2015.
 */
public class TextDeleteAdapter extends RecyclerView.Adapter<TextDeleteAdapter.ViewHolder>{
    List<List<SimpleTextModel>> simpleTextModels;
    List<TextDeleteModel> textDeleteModelList;
    int type;
    protected int footer = 0;

    /**
     * this is for add_product_category
     * @param deleteModels
     */
    public TextDeleteAdapter(List<TextDeleteModel> deleteModels){
        this(deleteModels, ProductActivity.ADD_PRODUCT_CATEGORY);
    }

    public TextDeleteAdapter(List<TextDeleteModel> deleteModels, int type){
        textDeleteModelList = deleteModels;
        this.type = type;
        simpleTextModels = new ArrayList<>();
    }

    public void setSimpleTextModels(List<SimpleTextModel> simpleTextModels) {
        this.simpleTextModels.add(simpleTextModels);
    }

    public List<SimpleTextModel> getSimpleTextModels(int position){
        return this.simpleTextModels.get(position);
    }

    public List<List<SimpleTextModel>> getSimpleTextModels() {
        return simpleTextModels;
    }

    public ArrayList<ArrayList<SimpleTextModel>> getParcelFormat(){
        ArrayList<ArrayList<SimpleTextModel>> temp = new ArrayList<>();
        if(checkNotNull(simpleTextModels)) {
            for (List<SimpleTextModel> a : simpleTextModels) {
                temp.add(new ArrayList<>(a));
            }
        }
        return temp;
    }

    public void removeSimpleTextModels(int position){
        this.simpleTextModels.remove(position);
    }

    public void clearSimpleTextModels(){
        this.simpleTextModels.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_text_delete, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(textDeleteModelList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return textDeleteModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextDeleteModel textDeleteModel;
        ImageView delete;
        TextView text;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView)itemView.findViewById(R.id.text_delete_text);
            delete = (ImageView)itemView.findViewById(R.id.text_delete_delete_btn);
            itemView.setOnClickListener(this);
//            text.setOnClickListener(this);
//            delete.setOnClickListener(this);
        }

        public void bindView(TextDeleteModel textDeleteModel, int position){
            this.textDeleteModel = textDeleteModel;
            this.position = position;
            if(textDeleteModel.isDefault()&& textDeleteModel.getCustomText() == null ){
                switch (type){
                    case ProductActivity.ADD_PRODUCT_CATEGORY:
                        text.setText(itemView.getContext().getResources().getStringArray(R.array.category_report)[0]);
                        break;
                    case ProductActivity.ADD_PRODUCT_CHOOSE_ETALASE:
                        text.setText(itemView.getContext().getResources().getString(R.string.title_action_add_window));
                        break;
                }
            } else if (textDeleteModel.isDefault()&& (textDeleteModel.getCustomText() != null)){
                switch (type) {
                    case ProductActivity.ADD_PRODUCT_CATEGORY:
                    case ProductActivity.ADD_PRODUCT_CHOOSE_ETALASE:
                        text.setText(Html.fromHtml(textDeleteModel.getCustomText()));
                        break;
                }
            } else {
                text.setText(Html.fromHtml(textDeleteModel.getText()));
            }
        }

        @Override
        public void onClick(View v) {
//            switch (v.getId()){
//                case R2.id.text_delete_delete_btn:
//                    // reset to certain position
//                    ((ProductActivity)itemView.getContext()).deleteSpinner(type, position);
//                    break;
//                case R2.id.text_delete_text:
                    // show pop up here
                    switch(type){
                        case ProductActivity.ADD_PRODUCT_CATEGORY:
                            if(itemView.getContext() != null
                                    && itemView.getContext() instanceof AppCompatActivity){
                                FragmentManager fm =
                                ((AppCompatActivity)itemView.getContext()).getSupportFragmentManager();

                                ProductActivity.showPopup(fm, ProductActivity.ADD_PRODUCT_CATEGORY,
                                        itemView.getContext().getResources().getStringArray(R.array.category_report)[0],
                                        simpleTextModels.get(position));
                            }
                            break;
                        case ProductActivity.ADD_PRODUCT_CHOOSE_ETALASE:
                            if (itemView.getContext() != null
                                    && itemView.getContext() instanceof AppCompatActivity) {
                                FragmentManager fm =
                                        ((AppCompatActivity)itemView.getContext()).getSupportFragmentManager();

                                String title = textDeleteModel.getCustomText() == null ?
                                        itemView.getContext().getResources().getString(R.string.title_action_add_window) :
                                        textDeleteModel.getCustomText();
                                ProductActivity.showPopup(fm, ProductActivity.ADD_PRODUCT_CHOOSE_ETALASE,
                                        title,
                                        simpleTextModels.get(position)
                                );
                            }
                            break;
                    }

//                    break;
//            }
        }
    }
}
