import React, { Component } from 'react'
import { connect } from 'react-redux'
import {
  StyleSheet,
  TouchableNativeFeedback,
  TouchableOpacity,
  Platform,
  AsyncStorage,
  Text,
  View,
} from 'react-native'
import { addToFavourite, removeFromFavourite } from '../../actions/actions'
import { NavigationModule } from 'NativeModules';

class FavouriteButton extends Component {
  _onTap = (isShopFav, shopId) => {
    const { dispatch } = this.props
    
    AsyncStorage.getItem('user_id').then(res => { 
      User_ID = res 
      if (User_ID != null) {
        if (isShopFav) {
          return dispatch(removeFromFavourite(shopId, User_ID))
        } else {
          return dispatch(addToFavourite(shopId, User_ID))
        }
      } else {
        NavigationModule.navigateToLoginWithResult()
      }
    })
  }

  render() {
    const isShopFav = this.props.isFav
    const shopId = this.props.shopId
    const Touchable = Platform.OS === 'android' ? TouchableNativeFeedback : TouchableOpacity

    return (
      <Touchable
        accessibilityComponentType="button"
        accessibilityLabel={isShopFav ? 'Favourited' : '+ Favourite'}
        onPress={() => this._onTap(isShopFav, shopId)}>
        <View style={isShopFav ? [styles.buttonBasic ,styles.removeButton] : [styles.buttonBasic, styles.addButton]}>
          <Text style={isShopFav ? [styles.textBasic, styles.removeText] : [styles.textBasic, styles.addText]}>{isShopFav ? '+ Favorit' : '+ Favoritkan'}</Text>
        </View>
      </Touchable>
    )
  }
}

const styles = StyleSheet.create({
   buttonBasic: {
     width: 105,
     borderWidth: 1,
     borderRadius: 3,
     padding: 5,
     borderColor: '#E0E0E0',
   },
   textBasic: {
     textAlign: 'center',
     fontSize: 13,
     fontWeight: '400',
     padding: 8
   },
  addButton: {
    backgroundColor: '#42b549',
    borderRadius: 3,
    borderColor: '#42b549',
    borderWidth: 1,
    marginVertical: 5
  },
  removeButton: {
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
    color: '#FFF'
  }
})
export default connect()(FavouriteButton)