import React, { Component } from 'react'
import { connect } from 'react-redux'
import {
  StyleSheet,
  TouchableNativeFeedback,
  TouchableOpacity,
  Platform,
  Text,
  View,
} from 'react-native'
import { addToFavourite, removeFromFavourite } from '../../actions/actions'

class FavouriteButton extends Component {

  _onTap = (isShopFav, shopId) => {
    const { dispatch, User_ID } = this.props
    if (isShopFav) {
      return dispatch(removeFromFavourite(shopId, User_ID))
    } else {
      return dispatch(addToFavourite(shopId, User_ID))
    }
  }

  render() {
    const isShopFav = this.props.isFav
    const shopId = this.props.shopId
    const Touchable = Platform.OS === 'android' ? TouchableNativeFeedback : TouchableOpacity
    // console.log(shopId + ' ' + isShopFav)

    return (
      <Touchable
        accessibilityComponentType="button"
        accessibilityLabel={isShopFav ? 'Favourited' : '+ Favourite'}
        onPress={() => this._onTap(isShopFav, shopId)}>
        <View style={isShopFav ? styles.removeButton : styles.addButton}>
          <Text style={isShopFav ? styles.removeText : styles.addText}>{isShopFav ? 'Favorit' : '+ Favoritkan'}</Text>
        </View>
      </Touchable>
    )
  }
}

const styles = StyleSheet.create({
  addButton: {
    // elevation: 4,
    backgroundColor: '#42b549',
    borderRadius: 3,
    borderColor: '#42b549',
    borderWidth: 1,
    marginVertical: 5
  },
  removeButton: {
    // elevation: 4,
    borderRadius: 3,
    borderColor: '#dedede',
    borderWidth: 1,
    marginVertical: 5
  },
  removeText: {
    color: '#999',
    textAlign: 'center',
    fontSize: 13,
    padding: 8,
  },
  addText: {
    color: 'white',
    textAlign: 'center',
    fontSize: 13,
    padding: 8,
    fontWeight: '500',
  }
})
export default connect()(FavouriteButton)