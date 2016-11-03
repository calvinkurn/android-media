package com.tokopedia.tkpd.database.model;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.tkpd.database.DatabaseConstant;
import com.tokopedia.tkpd.database.DbFlowDatabase;

/**
 * Created by m.normansyah on 12/27/15.
 */
@ModelContainer
@Table(database = DbFlowDatabase.class)
public class CategoryDB extends BaseModel implements DatabaseConstant {

    public static final String KATEGORI_NAMA_IDENTIFIER = "kategori_nama_identifier";
    public static final String KATEGORI_NAMA = "kategori_nama";
    public static final String LEVEL_ID = "level_id";
    public static final String CHILD_ID = "child_id";
    public static final String PARENT_ID = "parent_id";
    public static final String DEPARTMENT_ID = "department_id";
    public static final String KATEGORI = "kategori";
    public static final String GET_ID = KATEGORI + "."+ID;

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @Override
    public long getId() {
        return Id;
    }

    public CategoryDB(){super();}

    public CategoryDB(String nameCategory, int levelId, int childId, int parentId, int departmentId, String namaIdentifier) {
        super();
        this.nameCategory = nameCategory;
        this.levelId = levelId;
        this.childId = childId;
        this.parentId = parentId;
        this.departmentId = departmentId;
        this.categoryIdentifier = namaIdentifier;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Column
    public String nameCategory;
    @Column
    public int levelId;
    @Column
    public int childId;
    @Column
    public int parentId;
    @Column
    public int departmentId;

    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    @Column
    public String categoryIdentifier;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CategoryDB categoryDB = (CategoryDB) o;

        if (levelId != categoryDB.levelId) return false;
        if (childId != categoryDB.childId) return false;
        if (parentId != categoryDB.parentId) return false;
        if (departmentId != categoryDB.departmentId) return false;
        if (nameCategory != null ? !nameCategory.equals(categoryDB.nameCategory) : categoryDB.nameCategory != null) return false;
        return categoryIdentifier != null ? categoryIdentifier.equals(categoryDB.categoryIdentifier) : categoryDB.categoryIdentifier == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (nameCategory != null ? nameCategory.hashCode() : 0);
        result = 31 * result + levelId;
        result = 31 * result + childId;
        result = 31 * result + parentId;
        result = 31 * result + departmentId;
        result = 31 * result + (categoryIdentifier != null ? categoryIdentifier.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Kategori{" +
                "nama='" + nameCategory + '\'' +
                ", levelId=" + levelId +
                ", childId=" + childId +
                ", parentId=" + parentId +
                ", departmentId=" + departmentId +
                ", kategoriIdentifier='" + categoryIdentifier + '\'' +
                '}';
    }
}
