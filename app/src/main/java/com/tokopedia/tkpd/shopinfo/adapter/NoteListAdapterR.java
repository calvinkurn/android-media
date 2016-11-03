package com.tokopedia.tkpd.shopinfo.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.customadapter.AbstractRecyclerAdapter;
import com.tokopedia.tkpd.shopinfo.models.NoteModel;

import java.util.List;

/**
 * Created by Tkpd_Eka on 11/2/2015.
 */
public class NoteListAdapterR extends AbstractRecyclerAdapter{

    public interface NoteListAdapterInterface{
        void onView(int position);
        void onEdit(int position);
        void onDelete(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView notesName;
        ImageView editNotes;
        ImageView deleteNotes;

        public ViewHolder(View itemView) {
            super(itemView);
            notesName = (TextView) itemView.findViewById(R.id.notes_name);
            editNotes = (ImageView) itemView.findViewById(R.id.edit_notes);
            deleteNotes = (ImageView) itemView.findViewById(R.id.delete_notes);
        }
    }

    public static NoteListAdapterR createAdapter(List<NoteModel> list){
        NoteListAdapterR adapter = new NoteListAdapterR();
        adapter.modelList = list;
        return adapter;
    }

    List<NoteModel> modelList;
    private NoteListAdapterInterface listener;

    public void setListener(NoteListAdapterInterface listener){
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateVHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_manage_shop_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindVHolder(RecyclerView.ViewHolder holder, int position) {
        bindView(modelList.get(position), (ViewHolder)holder);
    }

    @Override
    public int getChildItemCount() {
        return modelList.size();
    }

    @Override
    public int getItemType(int pos) {
        return 0;
    }

    @Override
    public View.OnClickListener getRetryClickListener() {
        return null;
    }

    private void bindView(NoteModel model, ViewHolder holder){
        holder.notesName.setText(Html.fromHtml(model.title));
        holder.editNotes.setVisibility(View.GONE);
        holder.deleteNotes.setVisibility(View.GONE);
        setListener(holder);
    }

    private void setListener(final ViewHolder holder){
        holder.notesName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onView(holder.getAdapterPosition());
            }
        });
    }

}
