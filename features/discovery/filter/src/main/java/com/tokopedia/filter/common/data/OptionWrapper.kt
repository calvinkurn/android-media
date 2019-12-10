package com.tokopedia.filter.common.data

import com.tokopedia.design.list.item.SectionDividedItem

class OptionWrapper(var option: Option) : SectionDividedItem {
    override fun getSectionId(): Int {
        if(option.isPopular){
            return SectionDividedItem.POPULAR_SECTION_ID
        }

        if(option.name.isEmpty()){
            return '#'.toInt()
        }

        val sectionId = option.name.toUpperCase()[0]
        return if (sectionId >= 'A') {
            sectionId.toInt()
        } else {
            '#'.toInt()
        }
    }
}
