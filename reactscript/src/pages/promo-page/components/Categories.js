import React, { PureComponent } from 'react'
import { View, FlatList, ActivityIndicator, Text } from 'react-native'
import PropTypes from 'prop-types'
import Category from './Category'
import Footer from '../components/Infographics'
import BannerContainer from '../containers/BannerContainer'



class Categories extends PureComponent {
  componentDidMount() {
    const { offset, limit } = this.props.categories.pagination
    this.props.getCategories(offset, limit)
    this.props.getApplinkEnv()
  }

  renderCategory = ({ item }) => {
    const applink = this.props.applinkEnv

    console.log(item)
    // console.log(this.props.dataSlug)
    // console.log(applink)
    
    return <Category 
      category={item} 
      products={item.products}
      dataSlug={this.props.dataSlug} 
      applink={applink} />
  }

  loadMore = () => {
    if (this.props.categories.isFetching || !this.props.categories.canLoadMore) {
      return
    }
    const { offset, limit } = this.props.categories.pagination
    this.props.getCategories(offset, limit)
  }

  renderFooter = () => <Footer />

  renderHeader = () => (
    <View>
      <BannerContainer
        dataSlug={this.props.dataSlug}
        navigation={this.props.navigation}
        termsConditions={this.props.dataTermsConditions} />
    </View>
  )

  render() {
    const categories = this.props.categories.items
    return (<FlatList
      data={categories}
      keyExtractor={item => item.category_id}
      renderItem={this.renderCategory}
      onEndReached={this.loadMore}
      onEndReachedThreshold={0.5}
      ListFooterComponent={this.renderFooter}
      ListHeaderComponent={this.renderHeader}
      style={{backgroundColor: '#F8F8F8'}}
    />)
  }
}

Categories.propTypes = {
  getCategories: PropTypes.func.isRequired,
  categories: PropTypes.shape({
    items: PropTypes.array,
    isFetching: PropTypes.bool,
    pagination: PropTypes.shape({
      offset: PropTypes.number,
      limit: PropTypes.number,
    }),
  }).isRequired,
}

export default Categories