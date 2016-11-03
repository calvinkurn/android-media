package com.tokopedia.tkpd.rescenter.create.util;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.tkpd.database.model.AttachmentResCenterDB;
import com.tokopedia.tkpd.database.model.AttachmentResCenterDB_Table;

import java.util.List;

/**
 * Created on 7/12/16.
 */
public class LocalCacheManager {

    public static class AttachmentCreateResCenter {

        private String orderID;
        private String imageLocalPath;
        private String imageUrl;

        public AttachmentCreateResCenter() {
        }

        public static AttachmentCreateResCenter Builder(String orderID) {
            AttachmentCreateResCenter foo = new AttachmentCreateResCenter();
            foo.orderID = orderID;
            return foo;
        }

        public String getImageLocalPath() {
            return imageLocalPath;
        }

        public AttachmentCreateResCenter setImageLocalPath(String imageLocalPath) {
            this.imageLocalPath = imageLocalPath;
            return this;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public AttachmentCreateResCenter setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public void save() {
            AttachmentResCenterDB db = new AttachmentResCenterDB();
            db.orderID = orderID;
            db.imagePath = getImageLocalPath();
            db.imageUrl = getImageUrl();
            db.modulName = AttachmentResCenterDB.MODULE_CREATE_RESCENTER;
            db.save();
        }

        public List<AttachmentResCenterDB> getCache() {
            List<AttachmentResCenterDB> mList = new Select().from(AttachmentResCenterDB.class)
                    .where(AttachmentResCenterDB_Table.orderID.is(orderID))
                    .and(AttachmentResCenterDB_Table.modulName.is(AttachmentResCenterDB.MODULE_CREATE_RESCENTER))
                    .queryList();
            return mList;
        }

        public void clearAll() {
            for (AttachmentResCenterDB data : getCache()) {
                data.delete();
            }
        }

        public void remove(AttachmentResCenterDB attachmentReplyResCenterDB) {
            new Delete().from(AttachmentResCenterDB.class)
                    .where(AttachmentResCenterDB_Table.orderID.is(orderID))
                    .and(AttachmentResCenterDB_Table.modulName.is(AttachmentResCenterDB.MODULE_CREATE_RESCENTER))
                    .and(AttachmentResCenterDB_Table.id.is(attachmentReplyResCenterDB.getId()))
                    .execute();
        }
    }
}