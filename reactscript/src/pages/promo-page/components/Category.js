import React from 'react'
import { View, Text } from 'react-native'
import PropTypes from 'prop-types'
import ProductList from './ProductList'
import TKPButton from '../common/TKPPrimaryBtn'
import { NavigationModule } from 'NativeModules'


const url_categories_staging = 'https://staging.tokopedia.com/official-store/promo'
const url_categories_prod = 'https://tokopedia.com/official-store/promo'

const Category = ({ category, dataSlug }) => (
  <View style={{marginBottom: 10}}>
    <View style={{
      backgroundColor: '#FFF', borderTopWidth: 1, borderBottomWidth: 1, borderColor: '#e0e0e0', marginVertical: 10
    }}>
      <View style={{ paddingVertical: 15, paddingHorizontal: 10 }}>
        <Text numberOfLines={1} style={{ fontSize: 16, fontWeight: '600', color: 'rgba(0,0,0,.7)' }}>{category.category_name}</Text>
      </View>
      <ProductList products={category.products} />
    </View>

    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}} >
      <TKPButton content="View All" onTap={() => { 
        console.log(category)
        NavigationModule.navigateWithMobileUrl('', `${url_categories_staging}/${dataSlug.slug}/${category.slug}`, "")}}
        type="small" />
    </View>
  </View>
)


Category.propTypes = {
  category: PropTypes.shape({
    category_name: PropTypes.string.isRequired,
  }).isRequired,
}

export default Category