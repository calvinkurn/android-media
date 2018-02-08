import React, { Component } from 'react'
import {
  View,
  TextInput,
  StyleSheet,
  SectionList,
  TouchableWithoutFeedback,
  Image,
  Keyboard,
  FlatList
} from 'react-native'

import NotFound from './SearchNotFound'
import { Text } from '../../../common/TKPText'
import { icons } from '../../../lib/config'
import { NavigationModule } from 'NativeModules'



class SearchBar extends Component {
  constructor(props) {
    super(props)
    this.state = { showResults: false }
  }

  renderItem = ({ item }) => (
    <View style={{
      height: 80, borderBottomWidth: 1,
      borderBottomColor: '#e0e0e0',
      paddingLeft: '10%',
      justifyContent: 'center'
    }}>
      <TouchableWithoutFeedback onPress={() => {
          this.toggleResults(false)
          Keyboard.dismiss()
          NavigationModule.navigate(`posapp://product/${item.id}`, '')
        }}>
        <View>
          <Text
            style={{ fontSize: 14 }}
            ellipsizeMode='tail'
            numberOfLines={1}>{item.text}</Text>
        </View>
      </TouchableWithoutFeedback>
    </View>
  )


  toggleResults = (visible) => {
    this.setState({ showResults: visible })
  }

  render() {
    return (
      <View>
        <View>
          <TextInput
            placeholder='Cari Produk'
            inlineImageLeft='search_icon'
            inlineImagePadding={20}
            style={styles.searchTextBox}
            value={this.props.queryText}
            maxLength={30}
            returnKeyType='search'
            underlineColorAndroid='transparent'
            onSubmitEditing={
              () => {
                this.props.onSubmit(this.props.queryText, this.props.etalaseId)
                this.toggleResults(false)
              }
            }
            onChangeText={
              (text) => {
                if (text.length === 0) {
                  this.toggleResults(false)
                  this.props.onClearSearch()
                } else {
                  this.toggleResults(true)
                  this.props.onSearchType(text)
                  this.props.onSearch(text, this.props.etalaseId)
                }
              }
            }
          />
          <View style={styles.closeIconWrapper}>
            <TouchableWithoutFeedback onPress={() => {
              this.props.onClearSearch()
              this.toggleResults(false)
            }}>
              <Image source={{ uri: icons.close_icon }} />
            </TouchableWithoutFeedback>
          </View>
        </View>
        {
          this.state.showResults && (
            <View style={styles.results}>
              {
                this.props.items.length > 0 ? (<FlatList
                  keyboardShouldPersistTaps='always'
                  keyExtractor={(item) => item.id }
                  data={this.props.items}
                  renderItem={this.renderItem}
                />)
                  : (<NotFound />)
              }
            </View>
          )
        }
      </View>
    )
  }
}

const styles = StyleSheet.create({
  results: {
    position: 'absolute',
    left: 0,
    zIndex: 1001,
    top: 52,
    backgroundColor: '#fff',
    width: '100%',
    borderBottomWidth: 1,
    borderLeftWidth: 1,
    borderRightWidth: 1,
    borderColor: '#e0e0e0',
    maxHeight: 400,
    minHeight: 200,
  },
  searchTextBox: {
    height: 50,
    borderColor: '#e0e0e0',
    borderWidth: 1,
    fontSize: 16,
    backgroundColor: '#fff',
  },
  closeIconWrapper: {
    position: 'absolute',
    right: 10,
    top: 15,
  }
})

export default SearchBar