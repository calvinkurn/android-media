import React, { Component } from 'react'
import {
  View,
  TextInput,
  StyleSheet,
  SectionList,
  TouchableWithoutFeedback,
  Image,
  Keyboard
} from 'react-native'

import NotFound from './SearchNotFound'
import { Text } from '../../common/TKPText'

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
      <TouchableWithoutFeedback onPress={
        () => {
          this.props.onSearchItemTap(item)
          this.toggleResults(false)
          Keyboard.dismiss()
        }
      }>
        <View>
          <Text
            style={{ fontSize: 14 }}
            ellipsizeMode='tail'
            numberOfLines={1}>{item.text}</Text>
        </View>
      </TouchableWithoutFeedback>

    </View>
  )

  renderHeader = ({ section }) => (
    <View style={{
      paddingLeft: '10%',
      justifyContent: 'center',
      height: 78,
      borderBottomWidth: 1,
      borderBottomColor: '#e0e0e0'
    }}>
      <Text style={{
        fontSize: 14,
        fontWeight: '400',
        color: '#9b9b9b'
      }}>{section.title}</Text>
    </View>
  )

  toggleResults = (visible) => {
    this.setState({ showResults: visible })
  }

  render() {
    console.log(this.props.items)
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
              <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/close-icon.png' }} />
            </TouchableWithoutFeedback>
          </View>
        </View>
        {
          this.state.showResults && (
            <View style={styles.results}>
              {
                this.props.items.length > 0 ? (<SectionList
                  keyboardShouldPersistTaps='always'
                  keyExtractor={(item) => {
                    return item.id
                  }}
                  renderItem={this.renderItem}
                  renderSectionHeader={this.renderHeader}
                  sections={[ // homogenous rendering between sections
                    { data: this.props.items, title: 'Pencarian Terakhir' },
                    { data: [{ text: 'Samsung S4', id: 4 }], title: 'Popular Search', },
                  ]}
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
    borderColor: '#fff',
    borderWidth: 1,
    fontSize: 18,
    backgroundColor: '#fff',
  },
  closeIconWrapper: {
    position: 'absolute',
    right: 10,
    top: 15,
  }
})

export default SearchBar